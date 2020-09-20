package org.itstep.liannoi.maps.presentation.maps

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.SupportMapFragment
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import org.itstep.liannoi.maps.R
import org.itstep.liannoi.maps.application.common.maps.LocationProvider
import org.itstep.liannoi.maps.application.common.maps.MapClient
import org.itstep.liannoi.maps.databinding.FragmentMapsBinding
import org.itstep.liannoi.maps.infrastructure.maps.DefaultLocationProvider
import org.itstep.liannoi.maps.infrastructure.maps.DefaultMapClient
import org.itstep.liannoi.maps.infrastructure.maps.DefaultRoulette
import org.itstep.liannoi.maps.presentation.common.EventObserver

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var viewDataBinding: FragmentMapsBinding

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
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun setupMap() {
        viewModel.setupMap(provideMapClient(provideLocationProvider()))

        viewModel.measuredEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun provideMapClient(locationProvider: LocationProvider): MapClient = DefaultMapClient(
        childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment,
        locationProvider,
        DefaultRoulette(locationProvider)
    )

    private fun provideLocationProvider(): LocationProvider = DefaultLocationProvider(
        RxPermissions(this),
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    )
}
