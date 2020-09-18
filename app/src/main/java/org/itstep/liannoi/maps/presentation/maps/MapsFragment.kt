package org.itstep.liannoi.maps.presentation.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import org.itstep.liannoi.maps.R
import org.itstep.liannoi.maps.databinding.FragmentMapsBinding

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var viewDataBinding: FragmentMapsBinding
    private lateinit var mapFragment: SupportMapFragment

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
        setupMapFragment()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun setupMapFragment() {
        mapFragment =
            childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment
    }
}
