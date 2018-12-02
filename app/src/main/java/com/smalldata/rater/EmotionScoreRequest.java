package com.smalldata.rater;

public class EmotionScoreRequest {

    @Override
    public String toString() {
        return "{" +
                "\"anger\":" + anger +
                ", \"contempt\":" + contempt +
                ", \"disgust\":" + disgust +
                ", \"fear\":" + fear +
                ", \"happiness\":" + happiness +
                ", \"neutral\":" + neutral +
                ", \"sadness\":" + sadness +
                ", \"surprise\":" + surprise +
                '}';
    }

    public EmotionScoreRequest(double anger, double contempt, double disgust, double fear, double happiness, double neutral, double sadness, double surprise) {
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.surprise = surprise;
    }

    private double anger;

    private double contempt;

    private double disgust;

    private double fear;

    private double happiness;

    private double neutral;

    private double sadness;

    private double surprise;

    public double getAnger() {
        return anger;
    }

    public void setAnger(double anger) {
        this.anger = anger;
    }

    public double getContempt() {
        return contempt;
    }

    public void setContempt(double contempt) {
        this.contempt = contempt;
    }

    public double getDisgust() {
        return disgust;
    }

    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    public double getFear() {
        return fear;
    }

    public void setFear(double fear) {
        this.fear = fear;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public double getNeutral() {
        return neutral;
    }

    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    public double getSadness() {
        return sadness;
    }

    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    public double getSurprise() {
        return surprise;
    }

    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }
}