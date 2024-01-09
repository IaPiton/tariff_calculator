package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.outerDimension.OuterDimension;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.OuterDimensionPack;
import ru.fastdelivery.domain.delivery.pack.Pack;


import java.util.List;

/**
 * @param packages упаковки в грузе
 * @param currency валюта объявленная для груза
 */
public record Shipment(
        List<Pack> packages,

        List<OuterDimensionPack> outerDimensionPack,
        Currency currency
) {
    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }

    public OuterDimension allOuterDimensionPacks() {
        return outerDimensionPack.stream()
                .map(OuterDimensionPack::OuterDimension)
                .reduce(OuterDimension.zero(), OuterDimension::addOuterDimension);
    }
}
