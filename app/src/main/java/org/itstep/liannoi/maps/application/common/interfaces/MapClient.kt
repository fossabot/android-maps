package org.itstep.liannoi.maps.application.common.interfaces

import com.google.android.gms.maps.model.LatLng

interface MapClient : Disposable {

    fun request(notification: FineLocationNotification)

    fun prepare(notification: MapClickNotification)

    fun marker(latLng: LatLng)

    interface FineLocationNotification {

        fun onSuccess(isGranted: Boolean)
    }

    interface MapClickNotification {

        fun onClick(latLng: LatLng)
    }
}
