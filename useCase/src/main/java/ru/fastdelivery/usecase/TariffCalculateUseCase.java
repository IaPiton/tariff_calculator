package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;

    public Price calc(Shipment shipment, double distance) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var outerDimensionPackAllPackagesCubicMeters = shipment.allOuterDimensionPacks().OuterDimension();
        var minimalPrice = weightPriceProvider.minimalPrice();
        Price weightPrice = weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(minimalPrice);
        Price outerDimensionPrice = weightPriceProvider.outerDimensionPrice()
                .multiply(outerDimensionPackAllPackagesCubicMeters)
                .max(weightPrice);
        return distancePrice(outerDimensionPrice, distance, shipment.currency());
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }

    public Price distancePrice(Price outerDimensionPrice, double distance, Currency currency) {
        if (distance < 450) {
            return outerDimensionPrice;
        }
        BigDecimal tariffDistance = BigDecimal.valueOf(distance / 450);
        BigDecimal distancePrice = outerDimensionPrice.amount().multiply(tariffDistance).setScale(2, RoundingMode.CEILING);
        return new Price(distancePrice, currency);
    }


}
