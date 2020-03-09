package ch.epfl.sdp

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style


class MapboxActivity : AppCompatActivity() {

    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "sk.eyJ1IjoibXlocmFlbCIsImEiOiJjazdrZzd1Zm8wMXFrM2lxdzhkbTF3ajB3In0.Mq51a-NweZRy0i9OY90EPA")
        setContentView(ch.epfl.sdp.R.layout.activity_mapbox)
        mapView = findViewById<MapView>(ch.epfl.sdp.R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
            }
        })
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
        mapView!!.onSaveInstanceState(outState)
    }

}
