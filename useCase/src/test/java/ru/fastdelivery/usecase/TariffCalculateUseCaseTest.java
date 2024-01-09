package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.Departure;
import ru.fastdelivery.domain.common.distance.Destination;
import ru.fastdelivery.domain.common.outerDimension.OuterDimension;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.OuterDimensionPack;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(weightPriceProvider);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    @Test
    @DisplayName("Получение дистанции по координатам -> успешно")
    void calculateDistanceTest() {
        double expectedDistance = 1143.53;
        double START_LAT = 56.564;
        double START_LONG = 44.456;
        double END_LAT = 66.2566;
        double END_LONG = 51.666568;
        Destination destination = new Destination(START_LAT, START_LONG);
        Departure departure = new Departure(END_LAT, END_LONG);
        double actualDistance = tariffCalculateUseCase.calculateDistance(destination, departure);
        assertThat(actualDistance).isEqualTo(expectedDistance);
    }


    @Test
    @DisplayName("Расчет стоимости доставки по весу -> успешно")
    void whenCalculatePrice_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var outerDimensionPrice = new Price(BigDecimal.valueOf(1), currency);
        Destination destination = new Destination(1, 1);
        Departure departure = new Departure(1, 1);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.outerDimensionPrice()).thenReturn(outerDimensionPrice);
        List<OuterDimensionPack> outerDimensionPacks = Collections.singletonList(new OuterDimensionPack(new OuterDimension(new BigDecimal("100"))));
        var shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(1200)))),
                outerDimensionPacks,
                new CurrencyFactory(code -> true).create("RUB"));
        var expectedPrice = new Price(BigDecimal.valueOf(120), currency);
        var actualPrice = tariffCalculateUseCase.calculate(shipment, destination, departure);
        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки по объему -> успешно")
    void whenCalculateOuterDimensionPrice_thenSuccess() {
        var outerDimensionPrice = new Price(new BigDecimal("600"), currency);
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        Destination destination = new Destination(1, 1);
        Departure departure = new Departure(1, 1);
        when(weightPriceProvider.outerDimensionPrice()).thenReturn(outerDimensionPrice);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        OuterDimension outerDimension1 = new OuterDimension(new BigDecimal("0.929"));
        OuterDimensionPack outerDimensionPack1 = new OuterDimensionPack(outerDimension1);
        List<OuterDimensionPack> outerDimensionPacks = List.of(outerDimensionPack1);
        var shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(1200)))),
                outerDimensionPacks,
                new CurrencyFactory(code -> true).create("RUB"));
        var expectedPrice = new Price(new BigDecimal("557.4"), currency);
        var actualPrice = tariffCalculateUseCase.calculate(shipment, destination, departure);
        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки по растоянию свыше 450 км. -> успешно")
    void distancePriceTest() {
        Price price = new Price(new BigDecimal("555"), currency);
        Destination destination = new Destination(55, 65);
        Departure departure = new Departure(60, 72);
        Price expectedPrice = new Price(new BigDecimal("858.03"), currency);
        Price actualPrice = tariffCalculateUseCase.distancePrice(price, destination, departure, currency);
        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        var actual = tariffCalculateUseCase.minimalPrice();
        assertThat(actual).isEqualTo(minimalPrice);
    }
}