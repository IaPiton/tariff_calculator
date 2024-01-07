package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;

public record CargoPackage(
        @Schema(description = "Вес упаковки и объем, граммы и  миллимметры", example = "5667.45")
        BigInteger weight,
        BigInteger length,
        BigInteger width,
        BigInteger height

) {
}
