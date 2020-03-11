package ch.epfl.sdp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.geometry.VisibleRegion
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Projection
import com.mapbox.mapboxsdk.maps.Style


class MapboxActivity : AppCompatActivity() {

    private var mapView: MapView? = null
    private var downloadView: View? = null
    private var projection: Projection? = null

    fun backToHome(view: View?){
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun launchDL(view: View?){
        var region: VisibleRegion? = projection?.visibleRegion
        var bounds: LatLngBounds? = region?.latLngBounds

        var latN: TextView? = findViewById(ch.epfl.sdp.R.id.lat2_num)
        var latS: TextView? = findViewById(ch.epfl.sdp.R.id.lat1_num)
        var lngW: TextView? = findViewById(ch.epfl.sdp.R.id.lng1_num)
        var lngE: TextView? = findViewById(ch.epfl.sdp.R.id.lng2_num)

        latN?.text = bounds?.latNorth.toString()
        latS?.text = bounds?.latSouth.toString()
        lngW?.text = bounds?.lonWest.toString()
        lngE?.text = bounds?.lonEast.toString()

        downloadView?.visibility = View.VISIBLE
    }

    fun validateDL(view: View?){
        downloadView?.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "sk.eyJ1IjoibXlocmFlbCIsImEiOiJjazdrZzd1Zm8wMXFrM2lxdzhkbTF3ajB3In0.Mq51a-NweZRy0i9OY90EPA")
        setContentView(ch.epfl.sdp.R.layout.activity_mapbox)
        mapView = findViewById(ch.epfl.sdp.R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
            }
            projection = mapboxMap.projection
        })
        downloadView = findViewById(ch.epfl.sdp.R.id.dl_container)
        downloadView?.visibility = View.GONE
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
