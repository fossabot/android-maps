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
            mapClient.markedCommit(latLng)
            notifyClient()
        }

        ///////////////////////////////////////////////////////////////////////////
        // Helpers
        ///////////////////////////////////////////////////////////////////////////

        private fun notifyClient() {
            val content = "${mapClient.measure().toInt()} (m.)"
            Log.d(TAG, "MapClickNotificationHandler: $content")
            _measuredEvent.value = Event(content)
        }
    }

    private class FineLocationNotificationHandler : LocationProvider.FineLocationNotification {

        override fun onSuccess(isGranted: Boolean) {
            Log.d("FineLocationNotificationHandler: ", isGranted.toString())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Companion object
    ///////////////////////////////////////////////////////////////////////////

    companion object {

        private val TAG: String = MapsViewModel::class.simpleName!!
    }
}
