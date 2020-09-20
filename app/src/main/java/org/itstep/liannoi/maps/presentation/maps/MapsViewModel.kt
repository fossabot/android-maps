package org.itstep.liannoi.maps.presentation.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import org.itstep.liannoi.maps.application.common.maps.LocationProvider
import org.itstep.liannoi.maps.application.common.maps.MapClient
import org.itstep.liannoi.maps.presentation.common.Event

class MapsViewModel : ViewModel() {

    private val _measuredEvent = MutableLiveData<Event<String>>()
    val measuredEvent: LiveData<Event<String>> = _measuredEvent

    private lateinit var mapClient: MapClient

    fun setupMap(mapClient: MapClient) {
        this.mapClient = mapClient
        mapClient.request(FineLocationNotificationHandler())
        mapClient.prepare(MapClickNotificationHandler())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    override fun onCleared() {
        super.onCleared()
        mapClient.stop()
        mapClient.destroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal members
    ///////////////////////////////////////////////////////////////////////////

    private inner class MapClickNotificationHandler : MapClient.MapClickNotification {

        override fun onClick(latLng: LatLng) {
            mapClient.marker(latLng)
            mapClient.commit(latLng)
            _measuredEvent.value = Event("${mapClient.measure().toInt()} (m.)")
        }
    }

    private class FineLocationNotificationHandler : LocationProvider.FineLocationNotification {

        override fun onSuccess(isGranted: Boolean) {
            Log.d("FineLocationNotificationHandler: ", isGranted.toString())
        }
    }
}
