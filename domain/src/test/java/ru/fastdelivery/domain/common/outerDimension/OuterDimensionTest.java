package ru.fastdelivery.domain.common.outerDimension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;




public class OuterDimensionTest {
    BigInteger LENGTH = new BigInteger("1400");
    BigInteger WIDTH = new BigInteger("200");
    BigInteger HEIGTH = new BigInteger("226");
    @DisplayName("Если значение больше ноля -> false")
    @Test
  void isLessThanZeroTest(){
        assertThat(OuterDimension.isLessThanZero(LENGTH)).isFalse();
  }
    @DisplayName("Если значение меньше 1_500 -> true")
    @Test
    void isLessThanLargeSizeTest(){
        assertThat(OuterDimension.isLargeSize(WIDTH)).isFalse();
    }

    @DisplayName("Округление до 50")
    @Test
    void RoundingTest(){
        BigInteger expected = new BigInteger("250");
        assertThat(OuterDimension.rounding(HEIGTH)).isEqualTo(expected);
    }
    @DisplayName("Вычисление объема посылки")
    @Test
    void cubicMetersTest(){
        BigDecimal expected = new BigDecimal("0.0633");
        assertThat(OuterDimension.cubicMeters(WIDTH, HEIGTH, LENGTH)).isEqualTo(expected);
    }
}
