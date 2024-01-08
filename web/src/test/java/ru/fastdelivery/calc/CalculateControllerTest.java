package ru.fastdelivery.calc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.fastdelivery.ControllerTest;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.Departure;
import ru.fastdelivery.domain.common.distance.Destination;
import ru.fastdelivery.domain.common.distance.DistanceFactory;
import ru.fastdelivery.domain.common.outerDimension.OuterDimension;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.OuterDimensionPack;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.DistanceCalculateUseCase;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateControllerTest extends ControllerTest {

    final String baseCalculateApi = "/api/v1/calculate/";
    @MockBean
    TariffCalculateUseCase useCase;
    @MockBean
    CurrencyFactory currencyFactory;
    @MockBean
    DistanceFactory distanceFactory;

    DistanceCalculateUseCase distanceCalculateUseCase = new DistanceCalculateUseCase();

    @Test
    @DisplayName("Валидные данные для расчета стоимость -> Ответ 200")
    void whenValidInputData_thenReturn200() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new Destination(1,1),
                new Departure(1,10));
        List<Pack> packsWeights = request.packages().stream()
                .map(CargoPackage::weight)
                .map(Weight::new)
                .map(Pack::new)
                .toList();

        var rub = new CurrencyFactory(code -> true).create("RUB");
        OuterDimension outerDimension = new OuterDimension(new BigDecimal("1"));
        OuterDimensionPack outerDimensionPack = new OuterDimensionPack(outerDimension);
        List<OuterDimensionPack> outerDimensionPacks = List.of(outerDimensionPack);
        Destination destination = new Destination(request.destination().latitude(), request.destination().longitude());
        Departure departure = new Departure(request.departure().latitude(), request.departure().longitude());
        double distance = distanceCalculateUseCase.calculateDistance(destination.latitude(),destination.longitude(),
                departure.latitude(),departure.longitude());
        when(useCase.calc(new Shipment(packsWeights, outerDimensionPacks, rub), distance)).thenReturn(new Price(BigDecimal.valueOf(1), rub));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));
        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Список упаковок == null -> Ответ 400")
    void whenEmptyListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(null, "RUB", null,null);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
