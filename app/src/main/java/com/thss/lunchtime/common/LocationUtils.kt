package com.thss.lunchtime.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.thss.lunchtime.MainActivity
import java.io.IOException


object LocationUtils {

    private var currentLocation: Location? = null

    private fun isLocationPermissionGranted(context: Context, requestCode : Int): Boolean {
        val coarse = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        val fine = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        Log.d("LocationUtils", "coarse: $coarse, fine: $fine")
        return if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestCode
            )
            false
        } else {
            true
        }
    }
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): Location? {
        // initialize locationManager
        val locationManager = context.getSystemService(LocationManager::class.java)
        Log.d("LocationUtils", "is location permission granted: ${isLocationPermissionGranted(context, 0)}")
        if (isLocationPermissionGranted(context, 0)){
            val providers: List<String> = locationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val l: Location =
                    locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
            return bestLocation
        } else {
            return null
        }
    }

    fun getGeoFromLocation(context: Context, listener: (Location?, List<Address>) -> Unit) {
        val location = getCurrentLocation(context)
        if(location == null){
            Log.e("LocationUtils", "location is null")
            return
        }

        val geocoder = Geocoder(context)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    10
                ) { addressList ->
                    listener(location, addressList)
                }
            } else {
                val addressList = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    10
                )
                listener(location, addressList!!)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}