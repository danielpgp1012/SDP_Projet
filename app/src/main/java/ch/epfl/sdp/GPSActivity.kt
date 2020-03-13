package ch.epfl.sdp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class GPSActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null
    var location = Location(LocationManager.GPS_PROVIDER)
    var longitudeValueGPS: TextView? = null
    var latitudeValueGPS:TextView? = null
    private val requestCode = 1011


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        longitudeValueGPS = findViewById<TextView>(R.id.longitudeValueGPS)
        latitudeValueGPS = findViewById<TextView>(R.id.latitudeValueGPS)

        location.latitude = Double.NaN
        location.longitude = Double.NaN
        runOnUiThread {
            updateLocationInUI(location)
        }

        if(checkPermission()){
            locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2 * 1000, 10f, locationListenerGPS);
        }
    }

    fun goToMain(view: View?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Checks if the location permission is granted and returns a boolean indicating if it is the case
     */
    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == this.requestCode && checkPermission()){
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2 * 1000, 10f, locationListenerGPS)
        }
    }

    /**
     * update the values in the UI
     */
    private fun updateLocationInUI(location: Location){
        longitudeValueGPS?.text = location.longitude.toString()
        latitudeValueGPS?.text = location.latitude.toString()
    }

    private val locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(loc: Location) {
            location = loc
            runOnUiThread {
                updateLocationInUI(loc)
            }
        }
        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        // Gets triggered when the location permission is revoked while running
        override fun onProviderDisabled(s: String) {
            checkPermission()
            checkLocationSetting()
        }
    }

    /**
     * Checks if the location is enabled on the device and shows a pop-up asking to enable it if it
     * is not the case
     */
    private fun checkLocationSetting(): Boolean {
        val locationIsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)?:false
        if (!locationIsEnabled){
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                    .setTitle("Enable Location")
                    .setMessage("This part of the app cannot function without location, please enable it")
                    .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                        val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(myIntent)
                    }
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
            dialog.show()
        }
        return locationIsEnabled
    }
}
