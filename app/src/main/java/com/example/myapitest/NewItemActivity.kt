package com.example.myapitest

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.example.myapitest.databinding.ActivityNewItemBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class NewItemActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityNewItemBinding
    private lateinit var mMap: GoogleMap
    private var selectedMarker: Marker? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewItemBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Buscar localização do usuário - GPS, Wifi, 3G/4G
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setupView()
        setupGoogleMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        binding.mapContainer.visibility = View.VISIBLE
        mMap.setOnMapClickListener {
            latLng ->

            // Limpar marcador anterior, se existir
            selectedMarker?.remove()

            // Adicionar novo marcador
            selectedMarker = mMap.addMarker(
                MarkerOptions().position(latLng).draggable(true).title("${latLng.latitude}, ${latLng.longitude}")
            )
        }
        getDeviceLocation()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as com.google.android.gms.maps.SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getDeviceLocation() {
        if (checkSelfPermission(  this, ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            // Se já tem permissão, obter a localização atual
            loadCurrentLocation()
        } else {
            // Se não tem permissão, solicitar permissão
            // requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 1)
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadCurrentLocation() {
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            val currentLocationLatLng = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 15f))
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NewItemActivity::class.java)
    }
}
