package ru.fastdelivery.domain.common.distance;


import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DistanceFactory {
  private final  CheckingCoordinates checkingCoordinates;
     public Departure createDeparture (double latitude, double longitude){
        checkingCoordinates.isLongtitude(longitude);
        checkingCoordinates.isLatitude(latitude);
        return new Departure(latitude, longitude);
    }
    public Destination createDestination (double latitude, double longitude){
        checkingCoordinates.isLongtitude(longitude);
        checkingCoordinates.isLatitude(latitude);
        return new Destination(latitude, longitude);
    }


}
