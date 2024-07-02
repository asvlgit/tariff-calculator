package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.coordinate.CoordinateRestrictionsPropertiesProvider;

import java.math.BigDecimal;

/**
 * Ограничения из конфига
 */
@Configuration
@ConfigurationProperties("coordinate-restrictions")
@Setter
public class CoordinateRestrictionsProperties implements CoordinateRestrictionsPropertiesProvider {

    private BigDecimal minLatitude;
    private BigDecimal maxLatitude;
    private BigDecimal minLongitude;
    private BigDecimal maxLongitude;

    @Override
    public boolean isAvailable(BigDecimal latitude, BigDecimal longitude) {
        return latitude.compareTo(maxLatitude) <= 0 &&
                latitude.compareTo(minLatitude) >= 0 &&
                longitude.compareTo(minLongitude) >= 0 &&
                longitude.compareTo(maxLongitude) <= 0;
    }
}
