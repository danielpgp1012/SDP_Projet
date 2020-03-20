package ch.epfl.sdp

import android.annotation.SuppressLint
import io.mavsdk.System
import io.mavsdk.mission.Mission
import java.util.*
import java.util.concurrent.CountDownLatch


object DroneMissionExample {
    //must be IP address where the mavsdk_server is running
    //private val BACKEND_IP_ADDRESS = "127.0.0.1"
    private val BACKEND_IP_ADDRESS = "192.168.1.11"
    //private val BACKEND_IP_ADDRESS = "10.0.2.15"

    private val missionItems: ArrayList<Mission.MissionItem> = arrayListOf<Mission.MissionItem>()
    private var drone = System(BACKEND_IP_ADDRESS, 50020)

    fun makeDroneMission(): DroneMissionExample {
        addMissionItems()
        return this
    }

    fun startSimpleMission(){
        drone.getAction().arm().subscribe()
    }

    @SuppressLint("CheckResult")
    public fun startMission() {
        drone!!.mission
                .setReturnToLaunchAfterMission(true)
                .andThen(drone?.mission?.uploadMission(missionItems))
                .andThen(drone?.action?.arm())
                .andThen(drone?.mission?.startMission())
                .subscribe()

        val latch = CountDownLatch(1)
        drone!!.mission
                .missionProgress
                .filter { progress: Mission.MissionProgress -> progress.currentItemIndex === progress.missionCount }
                .take(1)
                .subscribe { ignored: Mission.MissionProgress? -> latch.countDown() }

        try {
            latch.await()
        } catch (ignored: InterruptedException) {
            // This is expected
        }

    }

    private fun generateMissionItem(latitudeDeg: Double, longitudeDeg: Double): Mission.MissionItem {
        return Mission.MissionItem(
                latitudeDeg,
                longitudeDeg,
                10f,
                10f,
                true, Float.NaN, Float.NaN,
                Mission.MissionItem.CameraAction.NONE, Float.NaN,
                1.0)
    }

    private fun addMissionItems() {
        missionItems.add(generateMissionItem(47.398039859999997, 8.5455725400000002))
        missionItems.add(generateMissionItem(47.398036222362471, 8.5450146439425509))
        missionItems.add(generateMissionItem(47.397825620791885, 8.5450092830163271))
        missionItems.add(generateMissionItem(47.397832880000003, 8.5455939999999995))
    }

}
