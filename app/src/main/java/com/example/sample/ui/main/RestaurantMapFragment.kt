package com.example.sample.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.sample.R
import com.example.sample.databinding.FragmentSecondBinding
import com.example.sample.ui.main.model.RestaurantListItemUiModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantMapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: RestaurantSearchViewModel by activityViewModels()
    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBarVisible(true)
        binding.mapView.getMapAsync(this)
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.employeesListUiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is RestaurantListUiState.Loading -> setProgressBarVisible(true)
                is RestaurantListUiState.Data -> updateDataState(uiState.restaurants, googleMap)
                is RestaurantListUiState.Empty -> updateEmptyState()
                is RestaurantListUiState.Error -> updateErrorState(uiState.errorMessage)
            }
        }
    }

    private fun updateDataState(employees: List<RestaurantListItemUiModel>, googleMap: GoogleMap) {
        setProgressBarVisible(false)
        binding.mapView.isVisible = true
        employees.forEach { uiModel ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(uiModel.latitude, uiModel.longitude))
                    .title(uiModel.name)
            )
        }
    }

    private fun updateEmptyState() {
        setProgressBarVisible(false)
        binding.mapView.isVisible = false
        binding.errorMessage.isVisible = true
        binding.errorMessage.text = getString(R.string.no_restaurants_found)
    }

    private fun updateErrorState(errorMessage: String) {
        setProgressBarVisible(false)
        binding.mapView.isVisible = false
        binding.errorMessage.isVisible = true
        binding.errorMessage.text = errorMessage
    }

    private fun setProgressBarVisible(isVisible: Boolean) {
        binding.listProgressBar.isVisible = isVisible
    }
}