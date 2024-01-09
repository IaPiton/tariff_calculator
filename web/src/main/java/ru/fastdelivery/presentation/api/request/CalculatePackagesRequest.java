package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.fastdelivery.domain.common.distance.Departure;
import ru.fastdelivery.domain.common.distance.Destination;



import java.util.List;

@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(

        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\", \"length\", \"width\", \"height\": 4056.45, 100, 200, 300, 400}]")
        @NotNull
        @NotEmpty
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull
        String currencyCode,
        @Schema(description = "Координаты места назначения", example = "60.398660, 55.027532")
        @NotNull
        Destination destination,
        @Schema(description = "Координаты места отправления", example = "60.398660, 55.027532")
        @NotNull
        Departure departure
) {
}
