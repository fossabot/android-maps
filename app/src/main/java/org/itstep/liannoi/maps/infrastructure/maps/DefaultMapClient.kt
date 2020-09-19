package org.itstep.liannoi.maps.infrastructure.maps

import android.annotation.SuppressLint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.itstep.liannoi.maps.application.common.maps.LocationProvider
import org.itstep.liannoi.maps.application.common.maps.MapClient
import org.itstep.liannoi.maps.application.common.maps.Roulette

class DefaultMapClient constructor(
    private val fragment: SupportMapFragment,
    private val locationProvider: LocationProvider,
    private val roulette: Roulette
) : MapClient {

    private lateinit var map: GoogleMap

    override fun request(notification: LocationProvider.FineLocationNotification) {
        locationProvider.request(notification)
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

    override fun commit(latLng: LatLng) {
        roulette.commit(latLng)
    }

    override fun measure(): Double = roulette.measure()

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    override fun stop() {
        locationProvider.stop()
    }

    override fun destroy() {
        locationProvider.destroy()
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
