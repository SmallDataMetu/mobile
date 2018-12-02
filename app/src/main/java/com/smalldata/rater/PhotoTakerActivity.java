package com.smalldata.rater;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class PhotoTakerActivity extends AppCompatActivity {
    private final String API_URL = "https://smalldata-hack.herokuapp.com";
    private ProgressDialog detectionProgressDialog;

    private String travelId;

    private final  String startTravelUrl = "/start-new-travel";

    private final String completeTravel = "/complete-travel?travelId=";

    private final String saveEmotionLogUrl = "/save-emotion-log";

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(BuildConfig.AzureApi, BuildConfig.AzureSubscriptionKey);

    private Uri file;

    private Button takePictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        detectionProgressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_taker);

        Intent intent = getIntent();
        String driverId = intent.getStringExtra("driverId");
        String vehicleId = intent.getStringExtra("vehicleId");

        postRequest(startTravelUrl, new TravelStater(driverId,vehicleId));

        takePictureButton = (Button) findViewById(R.id.button_image);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                try {
                    detectAndFrame(MediaStore.Images.Media.getBitmap(this.getContentResolver(), file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void postRequest(String path, Object postBody) {
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, postBody.toString());
        Request request = new Request.Builder()
                .url(API_URL + path)
                .post(body)
                .build();

        try {
            final RequestBody copy = request.body();
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);

            Response response = client.newCall(request).execute();

            if (path.equals(startTravelUrl)) {
                String sBody = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(sBody, Map.class);
                travelId = map.get("travelId").toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void endTravel(View view) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL + completeTravel + travelId)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Toast.makeText(PhotoTakerActivity.this,"Travel ended!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void detectAndFrame(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,
                                    false,
                                    new FaceServiceClient.FaceAttributeType[]{
                                            FaceServiceClient.FaceAttributeType.Emotion
                                    });

                            if (result == null) {
                                publishProgress(
                                        "Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(String.format(
                                    "Detection Finished. %d face(s) detected",
                                    result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        detectionProgressDialog.show();
                    }

                    @Override
                    protected void onProgressUpdate(String... progress) {
                        detectionProgressDialog.setMessage(progress[0]);
                    }

                    @Override
                    protected void onPostExecute(Face[] result) {
                        detectionProgressDialog.dismiss();

                        if (!exceptionMessage.equals("")) {
                            showError(exceptionMessage);
                        }
                        if (result == null) return;

                        List<EmotionScoreRequest> emotionScoreRequestList = new ArrayList<>();
                        for (Face face : result
                                ) {
                            emotionScoreRequestList.add(new EmotionScoreRequest(face.faceAttributes.emotion.anger,
                                    face.faceAttributes.emotion.contempt,
                                    face.faceAttributes.emotion.disgust,
                                    face.faceAttributes.emotion.fear,
                                    face.faceAttributes.emotion.happiness,
                                    face.faceAttributes.emotion.neutral,
                                    face.faceAttributes.emotion.sadness,
                                    face.faceAttributes.emotion.surprise));
                        }

                        SaveEmotionLogRequest saveEmotionLogRequest = new SaveEmotionLogRequest(travelId, emotionScoreRequestList);
                        postRequest(saveEmotionLogUrl, saveEmotionLogRequest);

                        imageBitmap.recycle();
                    }
                };

        detectTask.execute(inputStream);
    }


    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create().show();
    }

}
