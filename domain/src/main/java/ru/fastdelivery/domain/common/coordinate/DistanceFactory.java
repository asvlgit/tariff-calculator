package ru.fastdelivery.domain.common.coordinate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DistanceFactory {

    private final CoordinateRestrictionsPropertiesProvider coordinateRestrictionsPropertiesProvider;

    public Distance create(Departure departure, Destination destination) {
        if (departure == null) {
            throw new IllegalArgumentException("departure is null!");
        }

        if (destination == null) {
            throw new IllegalArgumentException("destination is null!");
        }

        if (!coordinateRestrictionsPropertiesProvider.isAvailable(departure.getLatitude(), departure.getLongitude())) {
            throw new IllegalArgumentException("Departure coordinates are not within the maximum and minimum limits!");
        }

        if (!coordinateRestrictionsPropertiesProvider.isAvailable(destination.getLatitude(), destination.getLongitude())) {
            throw new IllegalArgumentException("Destination coordinates are not within the maximum and minimum limits!");
        }

        return new Distance(departure, destination);
    }
}
