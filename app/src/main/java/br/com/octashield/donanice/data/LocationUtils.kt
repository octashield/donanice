package br.com.octashield.donanice.data

import android.location.Location

//object LocationUtils {
//    fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double, result: FloatArray) {
//        Location.distanceBetween(lat1, lon1, lat2, lon2, result)
//    }
//}

object LocationUtils {

    fun distanceBetween(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        results: FloatArray
    ) {
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results)
    }
}