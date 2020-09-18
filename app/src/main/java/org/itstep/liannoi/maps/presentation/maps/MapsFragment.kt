package org.itstep.liannoi.maps.presentation.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import org.itstep.liannoi.maps.R
import org.itstep.liannoi.maps.application.common.interfaces.MapClient
import org.itstep.liannoi.maps.databinding.FragmentMapsBinding
import org.itstep.liannoi.maps.presentation.common.maps.DefaultMapClient

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
        mapClient =
            DefaultMapClient(childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment)

        mapClient.request(FineLocationNotificationHandler())
        mapClient.prepare(MapClickNotificationHandler())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal members
    ///////////////////////////////////////////////////////////////////////////

    private class FineLocationNotificationHandler : MapClient.FineLocationNotification {

        override fun onSuccess(isGranted: Boolean) {
            Log.d("FineLocationNotificationHandler: ", isGranted.toString())
        }
    }

    private inner class MapClickNotificationHandler : MapClient.MapClickNotification {

        override fun onClick(latLng: LatLng) {
            mapClient.marker(latLng)
        }
    }
}
