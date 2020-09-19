package org.itstep.liannoi.maps.presentation.maps

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import org.itstep.liannoi.maps.R
import org.itstep.liannoi.maps.application.common.maps.LocationProvider
import org.itstep.liannoi.maps.application.common.maps.MapClient
import org.itstep.liannoi.maps.databinding.FragmentMapsBinding
import org.itstep.liannoi.maps.infrastructure.maps.DefaultLocationProvider
import org.itstep.liannoi.maps.infrastructure.maps.DefaultMapClient
import org.itstep.liannoi.maps.infrastructure.maps.DefaultRoulette

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var viewDataBinding: FragmentMapsBinding
    private lateinit var mapClient: MapClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentMapsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupMap()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    override fun onStop() {
        super.onStop()
        mapClient.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapClient.destroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun setupMap() {
        val locationProvider: LocationProvider = DefaultLocationProvider(
            RxPermissions(this),
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        )

        mapClient = DefaultMapClient(
            childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment,
            locationProvider,
            DefaultRoulette(locationProvider)
        )

        mapClient.request(FineLocationNotificationHandler())
        mapClient.prepare(MapClickNotificationHandler())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal members
    ///////////////////////////////////////////////////////////////////////////

    private class FineLocationNotificationHandler : LocationProvider.FineLocationNotification {

        override fun onSuccess(isGranted: Boolean) {
            Log.d("FineLocationNotificationHandler: ", isGranted.toString())
        }
    }

    private inner class MapClickNotificationHandler : MapClient.MapClickNotification {

        override fun onClick(latLng: LatLng) {
            mapClient.marker(latLng)
            mapClient.commit(latLng)
            val metres: Double = mapClient.measure()
            Log.d("Measure: ", metres.toString())
        }
    }
}
