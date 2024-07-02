package ru.fastdelivery.domain.common.coordinate;

import java.math.BigDecimal;

public interface CoordinateRestrictionsPropertiesProvider {
    boolean isAvailable(BigDecimal latitude, BigDecimal longitude);
}
