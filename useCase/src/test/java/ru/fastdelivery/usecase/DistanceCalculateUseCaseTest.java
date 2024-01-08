package ru.fastdelivery.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class DistanceCalculateUseCaseTest {
    final DistanceCalculateUseCase  distanceCalculateUseCase = new DistanceCalculateUseCase();

    @Test
    @DisplayName("Получение дистанции по координатам -> успешно")
    void calculateDistanceTest(){
       double expectedDistance = 1143.53;
        double START_LAT = 56.564;
        double START_LONG = 44.456;
        double END_LAT = 66.2566;
        double END_LONG = 51.666568;
        double actualDistance = distanceCalculateUseCase.calculateDistance(START_LAT, START_LONG, END_LAT, END_LONG);
        assertThat(actualDistance).isEqualTo(expectedDistance);

    }
}
