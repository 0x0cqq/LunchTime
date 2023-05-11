package com.thss.lunchtime.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.thss.lunchtime.MainActivity


object LocationUtils {

    private var currentLocation: Location? = null

    fun isLocationPermissionGranted(context: Context, requestCode : Int): Boolean {
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

        return if (isLocationPermissionGranted(context, 0)){
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            null
        }
    }

}