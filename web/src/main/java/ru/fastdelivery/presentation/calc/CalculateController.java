package ru.fastdelivery.presentation.calc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fastdelivery.domain.common.distance.DistanceFactory;
import ru.fastdelivery.domain.common.outerDimension.OuterDimension;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.OuterDimensionPack;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.DistanceCalculateUseCase;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calculate/")
@RequiredArgsConstructor
@Tag(name = "Расчеты стоимости доставки")
public class CalculateController {
    private final TariffCalculateUseCase tariffCalculateUseCase;
    private final CurrencyFactory currencyFactory;
    @Autowired
    private final DistanceFactory distanceFactory;

    private DistanceCalculateUseCase distanceCalculateUseCase = new DistanceCalculateUseCase();

    @PostMapping
    @Operation(summary = "Расчет стоимости по упаковкам груза")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation"), @ApiResponse(responseCode = "400", description = "Invalid input provided")})
    public CalculatePackagesResponse calculate(@Valid @RequestBody CalculatePackagesRequest request) {
        List<OuterDimensionPack> outerDimensionPacks = request.packages().stream()
                .map((CargoPackage t) -> new OuterDimension(OuterDimension.add(t.length(), t.width(), t.height()).OuterDimension()))
                .map(OuterDimensionPack::new)
                .toList();
        List<Pack> packsWeights = request.packages().stream()
                .map(CargoPackage::weight)
                .map(Weight::new)
                .map(Pack::new)
                .toList();
        var destination = distanceFactory.createDestination(request.destination().latitude(), request.destination().longitude());
        var departure = distanceFactory.createDeparture(request.departure().latitude(), request.departure().longitude());
        Double distance = distanceCalculateUseCase.calculateDistance(destination.latitude(), destination.longitude(),
                departure.latitude(), departure.longitude());
        Shipment shipment = new Shipment(packsWeights, outerDimensionPacks, currencyFactory.create(request.currencyCode()));
        var calculatedPrice = tariffCalculateUseCase.calc(shipment, distance);
        var minimalPrice = tariffCalculateUseCase.minimalPrice();
        return new CalculatePackagesResponse(calculatedPrice, minimalPrice);
    }
}

