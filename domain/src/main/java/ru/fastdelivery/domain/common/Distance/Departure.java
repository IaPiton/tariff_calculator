package ru.fastdelivery.domain.common.Distance;

public record Departure(double latitude,
                        double longitude) {

    public Departure (double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
