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
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*


class GPSActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null
    var longitudeGPS = 0.0
    var latitudeGPS = 0.0
    var longitudeValueGPS: TextView? = null
    var latitudeValueGPS:TextView? = null
    private val allPermissionResult = 1011


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        longitudeValueGPS = findViewById<TextView>(R.id.longitudeValueGPS)
        latitudeValueGPS = findViewById<TextView>(R.id.latitudeValueGPS)
        Log.d("------------------>>>>>", "Init")
/*
        val permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        */
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION), allPermissionResult)

        /*val permissionsToRequest = permissionsToRequest(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val size = permissionsToRequest.size
            val array: Array<String> = Array(size) { i:Int -> ""}
            requestPermissions(permissionsToRequest.
                    toArray(array), allPermissionResult)
        };*/

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
        locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2 * 1000, 10f, locationListenerGPS);
    }

    private fun permissionsToRequest(wantedPermissions: List<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (perm in wantedPermissions) {
            Log.d("------------------>>>>>", "Perm:$perm")
            if (!hasPermission(perm)) {
                Log.d("------------------>>>>>", "Failed:$perm")
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else true
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
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }

    fun testPermission(view: View) {

    }

    /*fun toggleGPSUpdates(view: View) {
            if (!checkLocation()) return
            val button = view as Button
            if (button.text == resources.getString(R.string.pause)) {
                locationManager.removeUpdates(locationListenerGPS)
                button.setText(R.string.resume)
            } else {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS)
                button.setText(R.string.pause)
            }
        }*/
    private val locationListenerGPS: LocationListener? = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longitudeGPS = location.longitude
            latitudeGPS = location.latitude
            Log.d("------------>>>>>","$longitudeGPS : $latitudeGPS")
            runOnUiThread {
                longitudeValueGPS?.text = longitudeGPS.toString() + ""
                latitudeValueGPS?.text = latitudeGPS.toString() + ""
                Toast.makeText(this@GPSActivity, "GPS Provider update", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }
}
