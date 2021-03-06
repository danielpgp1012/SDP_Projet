package ch.epfl.sdp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.ui.maps.SupportMapFragmentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


const val LATITUDE_TEST = "42.125"
const val LONGITUDE_TEST = "-30.229"
const val ZOOM_TEST = "0.9"

@RunWith(AndroidJUnit4::class)
class SupportMapFragmentActivityTest {
    var preferencesEditor: SharedPreferences.Editor? = null

    @get:Rule
    var mActivityRule = ActivityTestRule(
            SupportMapFragmentActivity::class.java,
            true,
            false) // Activity is not launched immediately

    @Before
    fun setUp() {
        val targetContext: Context = getInstrumentation().getTargetContext()
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit()
    }

    @Test
    fun mapboxUseOurPreferences() {
        preferencesEditor!!
                .putString("latitude", LATITUDE_TEST)
                .putString("longitude", LONGITUDE_TEST)
                .putString("zoom", ZOOM_TEST)
                .apply();

        // Launch activity
        mActivityRule.launchActivity(Intent())

        runOnUiThread {
            mActivityRule.activity.mapFragment.getMapAsync { mapboxMap ->
                assert(mapboxMap.cameraPosition.target.latitude.toString() == LATITUDE_TEST)
                assert(mapboxMap.cameraPosition.target.longitude.toString() == LONGITUDE_TEST)
                assert(mapboxMap.cameraPosition.zoom.toString() == ZOOM_TEST)
            }
        }
    }
}