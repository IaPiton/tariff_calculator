package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DistanceCalculateUseCase {


    double EARTH_RADIUS = 6378137;

   public double calculateDistance(double startLat, double startLong, double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));
        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);
        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c/1000;
    }
    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}