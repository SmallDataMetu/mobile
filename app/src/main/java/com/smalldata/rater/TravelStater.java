package com.smalldata.rater;

public class TravelStater
{
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTravelId() {
        return vehicleId;
    }

    public void setTravelId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    private String vehicleId;
    private String driverId;

    @Override
    public String toString() {
        return "{" +
                "\"vehicleId\":\"" +  vehicleId  +
                "\", \"driverId\":\"" + driverId +
                "\"}";
    }

    public TravelStater(String driverId, String vehicleId) {
        this.driverId = driverId;
        this.vehicleId = vehicleId;
    }

}
