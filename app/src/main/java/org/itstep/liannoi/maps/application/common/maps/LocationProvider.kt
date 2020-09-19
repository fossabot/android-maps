package org.itstep.liannoi.maps.application.common.maps

import com.google.android.gms.maps.model.LatLng
import org.itstep.liannoi.maps.application.common.interfaces.Disposable

interface LocationProvider : Disposable {

    fun request(notification: FineLocationNotification)

    fun current(): LatLng

    interface FineLocationNotification {

        fun onSuccess(isGranted: Boolean)
    }
}
