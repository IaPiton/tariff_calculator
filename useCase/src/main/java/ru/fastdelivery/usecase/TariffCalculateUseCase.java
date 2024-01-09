package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.distance.Departure;
import ru.fastdelivery.domain.common.distance.Destination;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    double EARTH_RADIUS = 6378137;

    public Price calculate(Shipment shipment, Destination destination, Departure departure) {
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
        return distancePrice(outerDimensionPrice, destination, departure, shipment.currency());
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }

    public Price distancePrice(Price outerDimensionPrice, Destination destination, Departure departure, Currency currency) {
        double distance = calculateDistance(destination, departure);
        if (distance < 450) {
            return outerDimensionPrice;
        }
        BigDecimal tariffDistance = BigDecimal.valueOf(distance / 450);
        BigDecimal distancePrice = outerDimensionPrice.amount().multiply(tariffDistance).setScale(2, RoundingMode.CEILING);
        return new Price(distancePrice, currency);
    }


    public double calculateDistance(Destination destination, Departure departure) {
        double startLat = destination.latitude();
        double startLong = destination.longitude();
        double endLat = departure.latitude();
        double endLong = departure.longitude();
        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));
        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);
        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = (EARTH_RADIUS * c / 1000);
        return new BigDecimal(distance).setScale(2, RoundingMode.DOWN).doubleValue();
    }

    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
