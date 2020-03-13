package ch.epfl.sdp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.geometry.VisibleRegion
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Projection
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils


class MapboxActivity : AppCompatActivity() {

    private var mapView: MapView? = null

    private var camera: CameraPosition? = null

    private var downloadView: View? = null
    private var projection: Projection? = null
    private var latN: TextView? = null
    private var latS: TextView? = null
    private var lngW: TextView? = null
    private var lngE: TextView? = null
    private var bounds: LatLngBounds? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ch.epfl.sdp.R.layout.activity_mapbox)
        Mapbox.getInstance(this, "sk.eyJ1IjoibXlocmFlbCIsImEiOiJjazduaGgyOWowMTk1M2xsOHQ5d2N6MW02In0.mOwke6u2usaPmvK9asaXew")
        mapView = findViewById(ch.epfl.sdp.R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
            }
            projection = mapboxMap.projection
            camera = mapboxMap.cameraPosition
        })
        downloadView = findViewById(ch.epfl.sdp.R.id.dl_container)
        downloadView?.visibility = View.GONE
        latN = findViewById(ch.epfl.sdp.R.id.lat2_num)
        latS = findViewById(ch.epfl.sdp.R.id.lat1_num)
        lngW = findViewById(ch.epfl.sdp.R.id.lng1_num)
        lngE = findViewById(ch.epfl.sdp.R.id.lng2_num)
    }

    fun listDlMaps(view: View?){

    }

    fun backToHome(view: View?){
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun launchDL(view: View?){
        bounds = projection?.visibleRegion?.latLngBounds
        mapView?.getMapAsync { mapboxMap -> camera = mapboxMap.cameraPosition }

        latN?.text = bounds?.latNorth.toString()
        latS?.text = bounds?.latSouth.toString()
        lngW?.text = bounds?.lonWest.toString()
        lngE?.text = bounds?.lonEast.toString()
        val zoom: TextView? = findViewById(ch.epfl.sdp.R.id.zoom_num)
        zoom?.text = camera?.zoom.toString()

        downloadView?.visibility = View.VISIBLE
    }

    fun validateDL(view: View?){
        // Define region of map tiles
        val definition = OfflineTilePyramidRegionDefinition(
                Style.OUTDOORS, bounds,
                camera!!.zoom,
                camera!!.zoom,
                resources.displayMetrics.density
        )
        // Customize the download notification's appearance
        val notificationOptions = NotificationOptions.builder(this)
                .smallIconRes(ch.epfl.sdp.R.drawable.mapbox_logo_icon)
                .returnActivity(MapboxActivity::class.java.name)
                .build()

        // Start downloading the map tiles for offline use
        OfflinePlugin.getInstance(this).startDownload(
                OfflineDownloadOptions.builder()
                        .definition(definition)
                        .metadata(OfflineUtils.convertRegionName("test_region"))
                        .notificationOptions(notificationOptions)
                        .build()
        )

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
