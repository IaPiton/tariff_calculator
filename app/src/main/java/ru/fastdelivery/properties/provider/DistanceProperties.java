package ru.fastdelivery.properties.provider;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.fastdelivery.domain.common.Distance.CheckingCoordinates;
import ru.fastdelivery.domain.common.Distance.DistanceFactory;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;

/**
 * Проверка координат по ограничениям
 */

@ConfigurationProperties("distance")
@Setter
public class DistanceProperties implements CheckingCoordinates {
    private double latitudeMin;
    private double latitudeMax;
    private double longitudeMin;
    private double longitudeMax;

    public DistanceProperties(double latitudeMin, double latitudeMax, double longitudeMin, double longitudeMax) {
        this.latitudeMin = latitudeMin;
        this.latitudeMax = latitudeMax;
        this.longitudeMin = longitudeMin;
        this.longitudeMax = longitudeMax;
    }

    @Override
    public boolean isLatitude(double latitude) {
        if (latitude < latitudeMin || latitude > latitudeMax) {
            throw new IllegalArgumentException("Latitude cannot be greater " + latitudeMax + " and less " + latitudeMin + "!");
        }
        return true;
    }

    @Override
    public boolean isLongtitude(double longitude) {
        if (longitude < longitudeMin || longitude > longitudeMax) {
            throw new IllegalArgumentException("Longitude cannot be greater " + longitudeMax + " and less " + longitudeMin + "!");
        }
        return true;
    }
}
