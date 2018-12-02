package com.smalldata.rater;

import java.util.List;

public class SaveEmotionLogRequest {

    private String travelId;

    @Override
    public String toString() {
        return "{" +
                "\"travelId\":\"" + travelId +
                "\", \"scores\":" + scores +
                "}";
    }

    private List<EmotionScoreRequest> scores;

    public SaveEmotionLogRequest(String travelId, List<EmotionScoreRequest> scores) {
        this.travelId = travelId;
        this.scores = scores;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public List<EmotionScoreRequest> getScores() {
        return scores;
    }

    public void setScores(List<EmotionScoreRequest> scores) {
        this.scores = scores;
    }
}


