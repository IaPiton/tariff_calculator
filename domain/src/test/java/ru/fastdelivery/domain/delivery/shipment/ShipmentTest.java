package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.outerDimension.OuterDimension;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.OuterDimensionPack;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);
        var packages = List.of(new Pack(weight1), new Pack(weight2));
        List<OuterDimensionPack> outerDimensionPacks = new ArrayList<>();
        var shipment = new Shipment(packages, outerDimensionPacks, new CurrencyFactory(code -> true).create("RUB"));
        var massOfShipment = shipment.weightAllPackages();
        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    void whenSummarizingOuterDimensionPackOfAllPackages_thenReturnSum() {
        var outerDimension1 = new OuterDimension(BigDecimal.TEN);
        var outerDimension2 = new OuterDimension(BigDecimal.ONE);
        var packages = List.of(new OuterDimensionPack(outerDimension1), new OuterDimensionPack(outerDimension2));
        List<Pack> weights = new ArrayList<>();
        var shipment = new Shipment(weights, packages, new CurrencyFactory(code -> true).create("RUB"));
        var massOfShipment = shipment.allOuterDimensionPacks();
        assertThat(massOfShipment.OuterDimension()).isEqualByComparingTo(BigDecimal.valueOf(11));
    }
}