package com.example.sample.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.sample.R
import com.example.sample.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: RestaurantSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        updateLocation()
        setUiClickListeners()

        binding.screenToggleButton.setOnClickListener {
            if (navController.currentDestination?.id == R.id.FirstFragment) {
                binding.screenToggleButton.text = getString(R.string.list)
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
            } else {
                binding.screenToggleButton.text = getString(R.string.map)
                navController.navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }

        viewModel.employeesListUiState.observe(this) {
            binding.screenToggleButton.isVisible = it is RestaurantListUiState.Data
        }
    }

    private fun setUiClickListeners() {
        val searchEditText = findViewById<EditText>(R.id.search_bar)
        val filterButton = findViewById<Button>(R.id.filter_button)

        filterButton.setOnClickListener {
            val searchText = searchEditText.text.toString()

            if (searchText.isNotEmpty()) {
                viewModel.searchRestaurantsByKeyword(searchText)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationPermissionRequest = registerForPermissionsResult()

        // Not ideal here - better to check which permission is enabled and proceed or request a specific one with rationale
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun registerForPermissionsResult(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            viewModel.onLocationUpdated(location.latitude, location.longitude)
                        } ?: viewModel.onLocationNotAcquired()
                    }
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            viewModel.onLocationUpdated(location.latitude, location.longitude)
                        } ?: viewModel.onLocationNotAcquired()
                    }
                }
                else -> {
                    // No location access granted.
                    viewModel.onPermissionsNotGranted()
                }
            }
        }
    }
}