package ru.fastdelivery.domain.common.coordinate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Distance(Departure departure, Destination destination) {

    private static final int RADIUS_EARTH_IN_METER = 6372795;

    public Distance {

        if (isLessThanZero(departure.latitude)) {
            throw new IllegalArgumentException("departure latitude cannot be below Zero!");
        }

        if (isLessThanZero(departure.longitude)) {
            throw new IllegalArgumentException("departure longitude cannot be below Zero!");
        }

        if (isLessThanZero(destination.latitude)) {
            throw new IllegalArgumentException("destination latitude cannot be below Zero!");
        }

        if (isLessThanZero(destination.longitude)) {
            throw new IllegalArgumentException("destination longitude cannot be below Zero!");
        }

    }

    private boolean isLessThanZero(BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) > 0;
    }

    private double calcHaversineFormulaWithModForAntipodes() {

        double latitudeInRadians1 = Math.toRadians(departure.latitude.doubleValue());
        double longitudeInRadians1 = Math.toRadians(departure.longitude.doubleValue());
        double latitudeInRadians2 = Math.toRadians(destination.latitude.doubleValue());
        double longitudeInRadians2 = Math.toRadians(destination.longitude.doubleValue());

        double cosLat1 = Math.cos(latitudeInRadians1);
        double cosLat2 = Math.cos(latitudeInRadians2);
        double sinLat1 = Math.sin(latitudeInRadians1);
        double sinLat2 = Math.sin(latitudeInRadians2);

        double deltaLon = longitudeInRadians2 - longitudeInRadians1;
        double cosDelta = Math.cos(deltaLon);
        double sinDelta = Math.sin(deltaLon);

        double y = Math.sqrt(Math.pow(cosLat2 * sinDelta, 2) + Math.pow(cosLat1 * sinLat2 - sinLat1 * cosLat2 * cosDelta, 2));
        double x = sinLat1 * sinLat2 + cosLat1 * cosLat2 * cosDelta;

        return Math.atan2(y, x) * RADIUS_EARTH_IN_METER;
    }

    public BigDecimal asKilometer() {
        return BigDecimal.valueOf(calcHaversineFormulaWithModForAntipodes())
                .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal asMeter() {
        return BigDecimal.valueOf(calcHaversineFormulaWithModForAntipodes());
    }
}
