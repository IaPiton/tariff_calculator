package ru.fastdelivery.properties_provider;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.properties.provider.DistanceProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistancePropertiesTest {
    public static final double LATITUDE_MIN = 20;
    public static final double LATITUDE_MAX = 50;
    public static final double LONGITUDE_MIN = 50;
    public static final double LONGITUDE_MAX = 90;
    public static final double DESTINATION_LONGITUDE = 30;
    public static final double DESTINATION_LATITUDE = 90;
    public static final double DEPARTURE_LATITUDE = 40;
    public static final double DEPARTURE_LONGITUDE = 95;

    DistanceProperties properties;

    @Test
    @DisplayName("Если широта в допустимом диапазоне -> true")
    void isLatitudeTest() {
        properties = new DistanceProperties(LATITUDE_MIN, LATITUDE_MAX, LONGITUDE_MIN, LONGITUDE_MAX);
        assertThat(properties.isLatitude(DEPARTURE_LATITUDE)).isTrue();
    }

    @Test
    @DisplayName("Если долгота в допустимом диапазоне -> true")
    void isLongitudeTest() {
        properties = new DistanceProperties(LATITUDE_MIN, LATITUDE_MAX, LONGITUDE_MIN, LONGITUDE_MAX);
        assertThat(properties.isLatitude(DESTINATION_LONGITUDE)).isTrue();
    }

    @Test
    @DisplayName("Если широта не в допустимом диапазоне -> исключение")
    void isLatitudeExceptionTest() {
        properties = new DistanceProperties(LATITUDE_MIN, LATITUDE_MAX, LONGITUDE_MIN, LONGITUDE_MAX);
        assertThrows(IllegalArgumentException.class,
                () -> properties.isLatitude(DESTINATION_LATITUDE));
    }

    @Test
    @DisplayName("Если долгота не в допустимом диапазоне -> исключение")
    void isLongitudeExceptionTest() {
        properties = new DistanceProperties(LATITUDE_MIN, LATITUDE_MAX, LONGITUDE_MIN, LONGITUDE_MAX);
        assertThrows(IllegalArgumentException.class,
                () -> properties.isLongitude(DEPARTURE_LONGITUDE));
    }
}
