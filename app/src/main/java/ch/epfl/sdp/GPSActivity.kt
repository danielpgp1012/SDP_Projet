package ch.epfl.sdp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class GPSActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null
    private var longitudeGPS = 0.0
    private var latitudeGPS = 0.0
    private var longitudeValueGPS: TextView? = null
    private var latitudeValueGPS: TextView? = null
    private val allPermissionResult = 1011


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        longitudeValueGPS = findViewById(R.id.longitudeValueGPS)
        latitudeValueGPS = findViewById(R.id.latitudeValueGPS)

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), allPermissionResult)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        checkLocation()

        locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2 * 1000, 10f, locationListenerGPS)
    }


    fun goToMain(view: View?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    }
    private fun showAlert() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
                .setMessage("This part of the app cannot function without location, please enable it")
                .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }

    private val locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longitudeGPS = location.longitude
            latitudeGPS = location.latitude
            runOnUiThread {
                longitudeValueGPS?.text = longitudeGPS.toString()
                latitudeValueGPS?.text = latitudeGPS.toString()
                //Toast.makeText(this@GPSActivity, "GPS Provider update", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }
}
