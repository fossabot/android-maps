package org.itstep.liannoi.maps.application.common.interfaces

interface MapClient : Disposable {

    fun request(notification: FineLocationNotification)

    fun prepare()

    interface FineLocationNotification {

        fun onSuccess(isGranted: Boolean)
    }
}
