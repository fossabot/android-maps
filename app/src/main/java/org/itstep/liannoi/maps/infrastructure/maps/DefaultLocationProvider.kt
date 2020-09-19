package org.itstep.liannoi.maps.infrastructure.maps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import org.itstep.liannoi.maps.application.common.maps.LocationProvider

class DefaultLocationProvider constructor(
    private val permissions: RxPermissions,
    private val locationManager: LocationManager
) : LocationProvider {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun request(notification: LocationProvider.FineLocationNotification) {
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { notification.onSuccess(it) }
            .addTo(disposable)
    }

    @SuppressLint("MissingPermission")
    override fun current(): LatLng {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10.0F, {})

        val location: Location =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

        return LatLng(location.latitude, location.longitude)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    override fun stop() {
        disposable.clear()
    }

    override fun destroy() {
        disposable.dispose()
    }
}
