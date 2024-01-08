package ru.fastdelivery.domain.common.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DistanceFactoryTest {
    CheckingCoordinates checkingCoordinates = mock(CheckingCoordinates.class);
    DistanceFactory factory = new DistanceFactory(checkingCoordinates);
    private final double latitude = 40;
    private final double longitude = 50;

    @Test
    @DisplayName("Если значение долготы и широты в разрешенном диапазонето объект создается")
    void whenTheCoordinatesAreInTheRange_ThatDepartureIsCreated() {
        when(checkingCoordinates.isLatitude(latitude)).thenReturn(true);
        when(checkingCoordinates.isLongtitude(longitude)).thenReturn(true);
        assertThat(factory.createDeparture(latitude, longitude)).isNotNull();
    }

    @Test
    @DisplayName("Если значение долготы и широты в разрешенном диапазонето объект создается")
    void whenTheCoordinatesAreInTheRange_ThatDestinationIsCreated() {
        when(checkingCoordinates.isLatitude(latitude)).thenReturn(true);
        when(checkingCoordinates.isLongtitude(longitude)).thenReturn(true);
        assertThat(factory.createDeparture(latitude, longitude)).isNotNull();
    }


}
