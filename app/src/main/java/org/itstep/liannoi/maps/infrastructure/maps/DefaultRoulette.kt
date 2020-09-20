package org.itstep.liannoi.maps.infrastructure.maps

import com.google.android.gms.maps.model.LatLng
import org.itstep.liannoi.maps.application.common.maps.LocationProvider
import org.itstep.liannoi.maps.application.common.maps.Roulette
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DefaultRoulette constructor(
    private val locationProvider: LocationProvider
) : Roulette {

    private val coordinates: MutableList<LatLng> = mutableListOf()

    override fun commit(latLng: LatLng): List<LatLng> {
        if (coordinates.size < 2) {
            coordinates.add(0, locationProvider.current())
        }

        coordinates.add(latLng)

        return coordinates.takeLast(2)
    }

    override fun measure(): Double = measure(coordinates)

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    /**
     * https://stackoverflow.com/a/11172685
     */
    private fun measure(first: LatLng, second: LatLng): Double {
        val dLat: Double = second.latitude * Math.PI / 180 - first.latitude * Math.PI / 180
        val dLon: Double = second.longitude * Math.PI / 180 - first.longitude * Math.PI / 180

        val a: Double = sin(dLat / 2) *
                sin(dLat / 2) +
                cos(first.latitude * Math.PI / 180) *
                cos(second.latitude * Math.PI / 180) *
                sin(dLon / 2) *
                sin(dLon / 2)

        return MapsDefaults.RADIUS_EARTH_KILOMETERS *
                (2 * atan2(sqrt(a), sqrt(1 - a))) *
                MapsDefaults.METERS_IN_KILOMETER
    }

    private fun measure(coordinates: List<LatLng>): Double {
        var result = 0.0

        (0 until coordinates.size - 1).forEach {
            result += measure(coordinates[it], coordinates[it + 1])
        }

        return result
    }
}
