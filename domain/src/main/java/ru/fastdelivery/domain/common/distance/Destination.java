package ru.fastdelivery.domain.common.distance;



public record Destination (double latitude, double longitude) {
      public Destination(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
