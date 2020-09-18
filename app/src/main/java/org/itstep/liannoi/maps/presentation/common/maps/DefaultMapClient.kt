package org.itstep.liannoi.maps.presentation.common.maps

import android.Manifest
import android.annotation.SuppressLint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import org.itstep.liannoi.maps.application.common.interfaces.MapClient

class DefaultMapClient constructor(
    private val fragment: SupportMapFragment
) : MapClient {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var map: GoogleMap

    override fun request(notification: MapClient.FineLocationNotification) {
        RxPermissions(fragment).request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { notification.onSuccess(it) }
            .addTo(disposable)
    }

    override fun prepare(notification: MapClient.MapClickNotification) {
        fragment.getMapAsync {
            initialize(it)
            setup()
            subscribe(notification)
        }
    }

    override fun marker(latLng: LatLng) {
        map.addMarker(MarkerOptions().position(latLng))
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

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun initialize(map: GoogleMap) {
        this.map = map
    }

    private fun subscribe(notification: MapClient.MapClickNotification) {
        map.setOnMapClickListener { notification.onClick(it) }
    }

    @SuppressLint("MissingPermission")
    private fun setup() {
        map.isMyLocationEnabled = true
    }
}
