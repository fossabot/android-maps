package org.itstep.liannoi.maps.application.common.maps

import com.google.android.gms.maps.model.LatLng
import org.itstep.liannoi.maps.application.common.interfaces.Disposable

interface MapClient : Disposable {

    fun request(notification: LocationProvider.FineLocationNotification)

    fun prepare(notification: MapClickNotification)

    fun marker(latLng: LatLng)

    fun commit(latLng: LatLng)

    fun measure(): Double

    interface MapClickNotification {

        fun onClick(latLng: LatLng)
    }
}
