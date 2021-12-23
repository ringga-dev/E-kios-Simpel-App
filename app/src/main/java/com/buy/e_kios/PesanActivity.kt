package com.buy.e_kios

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.buy.e_kios.data.api.RetrofitClient
import com.buy.e_kios.data.models.keranjang.Keranjang
import com.buy.e_kios.data.models.register.Register
import com.buy.e_kios.db.CompanionString.Companion.DEFAULT_INTERVAL_IN_MILLISECONDS
import com.buy.e_kios.db.CompanionString.Companion.DEFAULT_MAX_WAIT_TIME
import com.buy.e_kios.db.DBHelper
import com.buy.e_kios.db.SharedPrefManager
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_pesan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.location.LocationManager
import android.content.DialogInterface
import android.provider.Settings
import android.util.Log


class PesanActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private val REQUEST_CODE = 5678

    private var keranjang: MutableList<Keranjang> = ArrayList()
    private lateinit var map: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationEngine: LocationEngine
    private lateinit var callback: LocationChangeListeningCallback

    private var lat: Double? = null
    private var long: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(
            this,
            "pk.eyJ1IjoicmluZ2dhZGV2OTciLCJhIjoiY2t0aHlwNmViMHd3aDJ6cW5rZDE0N3RrYyJ9.yNc4BwWT45ZnF8JO6mpC9w"
        )
        setContentView(R.layout.activity_pesan)
        mapView.getMapAsync(this)
        getData()

        btn_pesan.setOnClickListener {
            pesan()
        }
        checkGPSStatus()
    }

    private fun checkGPSStatus() {
        var locationManager: LocationManager? = null
        var gps_enabled = false
        var network_enabled = false
        if (locationManager == null) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }
        try {
            network_enabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }
        if (!gps_enabled && !network_enabled) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("GPS OFF")
                .setMessage("GPS dalam ke adaan mati...?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->

                        startActivity(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        startActivity(Intent(this,KeranjangActivity::class.java))
                        finish()
                    })
            val alert = builder.create()
            alert.show()

        }
    }

    private fun pesan() {
        if (lat == null && long == null) {
            mapView.getMapAsync(this)

            Toast.makeText(this, "lokasi belum di dapatkan", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                mapView.getMapAsync(this)
            }, 10000)

        } else {
            val myProfile = SharedPrefManager.getInstance(this)?.user
            val alamat = et_alamat.text.toString().trim()
            val pesan = et_message.text.toString().trim()
            keranjang.forEach {
                RetrofitClient.instance.pesan(
                    it.id_barang,
                    myProfile!!.id,
                    it.qty,
                    alamat,
                    lat,
                    long,
                    pesan
                )
                    .enqueue(object : Callback<Register> {
                        override fun onResponse(
                            call: Call<Register>,
                            response: Response<Register>
                        ) {
                            if (response.body()?.stts == true) {
                                val db = DBHelper(this@PesanActivity, null)
                                db.delete()
                                Toast.makeText(
                                    this@PesanActivity,
                                    response.body()?.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@PesanActivity, HomeActivity::class.java))
                            } else {
                                Toast.makeText(
                                    this@PesanActivity,
                                    response.body()?.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Register>, t: Throwable) {
                            Toast.makeText(this@PesanActivity, t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
            }

        }
    }


    @SuppressLint("Range")
    private fun getData() {
        keranjang?.clear()
        val db = DBHelper(this, null)
        var hargatotal = 0
        val cursor = db.getName()
        cursor!!.moveToFirst()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                val barang = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_BARANG))
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.NAMA))
                val harga = cursor.getInt(cursor.getColumnIndex(DBHelper.HARGA))
                val image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE))
                val promo = cursor.getInt(cursor.getColumnIndex(DBHelper.PROMO))
                val qty = cursor.getInt(cursor.getColumnIndex(DBHelper.QTY))
                keranjang.add(Keranjang(id, barang, name, harga, image, promo, qty))

                val kurang = harga * promo / 100
                hargatotal += (harga - kurang) * qty
            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        callback = LocationChangeListeningCallback()
        mapboxMap.setStyle(Style.SATELLITE_STREETS) {
            enableLocationComponent(it)
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .build()
            map.locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled =
                    true                       // Enable to make component visible
                cameraMode =
                    CameraMode.TRACKING                        // Set the component's camera mode
                renderMode =
                    RenderMode.COMPASS                         // Set the component's render mode
            }
            initLocationEngine()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        locationEngine.requestLocationUpdates(request, callback, mainLooper)
        locationEngine.getLastLocation(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private inner class LocationChangeListeningCallback :
        LocationEngineCallback<LocationEngineResult> {

        override fun onSuccess(result: LocationEngineResult?) {
            result?.lastLocation
                ?: return //BECAREFULL HERE, IF NAME LOCATION UPDATE DONT USER -> val resLoc = result.lastLocation ?: return
            if (result.lastLocation != null) {
                lat = result.lastLocation?.latitude!!
                long = result.lastLocation?.longitude!!
                val latLng = LatLng(lat!!, long!!)

                if (result.lastLocation != null) {
                    map.locationComponent.forceLocationUpdate(result.lastLocation)
                    val position = CameraPosition.Builder()
                        .target(latLng)
//                        .zoom(13.0) //disable this for not follow zoom
                        .tilt(10.0)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                }
            }

        }

        override fun onFailure(exception: Exception) {}
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Permission not granted!!", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            map.getStyle {
                enableLocationComponent(it)
            }
        } else {
            Toast.makeText(this, "Permission not granted!! app will be EXIT", Toast.LENGTH_LONG)
                .show()
            Handler().postDelayed({
                finish()
            }, 3000)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Prevent leaks
        locationEngine.removeLocationUpdates(callback)
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}