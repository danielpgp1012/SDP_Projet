package ch.epfl.sdp

import android.content.Context
import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.filterEquals
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private var mUiDevice: UiDevice? = null

    @get:Rule
    val mActivityRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun before() {
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    private fun getContext(): Context{
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun canOpenSettings(){
        openActionBarOverflowOrOptionsMenu(getContext())
        onView(withText("Settings")).perform(click())
        intended(hasComponent(SettingsActivity::class.qualifiedName))
    }

    private fun openDrawer(){
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Check that drawer is closed to begin with
                .perform(DrawerActions.open())
    }

    @Test
    fun canNavigateToHome(){
        openDrawer()
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_home));
    }

    @Test
    fun canNavigateToMissionDesign(){
        openDrawer()
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_misson_design));
    }

    @Test
    fun canNavigateToMapsManaging(){
        openDrawer()
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_maps_managing));
    }

    private fun getGSO(): GoogleSignInOptions {
        return GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("272117878019-uf5rlbbkl6vhvkkmin8cumueil5ummfs.apps.googleusercontent.com")
                .requestEmail()
                .build()
    }

    @Test
    fun clickOnUserProfilePictureOpensLoginMenu(){
        openDrawer()
        onView(withId(R.id.nav_user_image)).perform(click())

        val mGoogleSignInClient = GoogleSignIn.getClient(getContext(), getGSO())
        intended(filterEquals(mGoogleSignInClient.signInIntent))
        mUiDevice?.pressBack()
    }

    @Test
    fun clickOnUserEmailOpensLoginMenu(){
        openDrawer()
        onView(withId(R.id.nav_user_email)).perform(click())

        val mGoogleSignInClient = GoogleSignIn.getClient(getContext(), getGSO())
        intended(filterEquals(mGoogleSignInClient.signInIntent))
        mUiDevice?.pressBack()
    }

    @Test
    fun clickOnUsernameOpensLoginMenu(){
        openDrawer()
        onView(withId(R.id.nav_username)).perform(click())

        val mGoogleSignInClient = GoogleSignIn.getClient(getContext(), getGSO())
        intended(filterEquals(mGoogleSignInClient.signInIntent))
        mUiDevice?.pressBack()
    }

    @Test
    fun updateUserViewUpdatesUserInformationInDrawer(){
        val dummyUserName = "dummy_username"
        val dummyEmail = "dummy_email"

        openDrawer()

        runOnUiThread{
            mActivityRule.activity.updateUserView(dummyUserName, dummyEmail)
        }
        onView(withId(R.id.nav_username)).check(matches(withText(dummyUserName)))
        onView(withId(R.id.nav_user_email)).check(matches(withText(dummyEmail)))
    }

    @Test
    fun clickingTheHamburgerOpensTheDrawer(){
        runOnUiThread{
            mActivityRule.activity.onSupportNavigateUp()
        }
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }
}