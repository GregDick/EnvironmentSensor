package com.example.greg.environmentsensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private var pressure: Sensor? = null
    private var light: Sensor? = null
    private var magneticField: Sensor? = null
    private var proximity: Sensor? = null
    private var gyro: Sensor? = null
    private var grav: Sensor? = null
    val df = DecimalFormat("0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        grav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.d("SENSORS", "device sensor list: $deviceSensors")
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when(it.sensor.type) {
                Sensor.TYPE_LIGHT -> updateLight(it)
                Sensor.TYPE_PRESSURE -> updatePressure(it)
                Sensor.TYPE_MAGNETIC_FIELD -> updateMagnet(it)
                Sensor.TYPE_PROXIMITY -> updateProximity(it)
                Sensor.TYPE_GYROSCOPE -> updateGyro(it)
                Sensor.TYPE_GRAVITY -> updateGravity(it)
            }
        }
    }

    private fun updateGravity(it: SensorEvent) {
        val gravX = df.format(it.values[0])
        val gravY = df.format(it.values[1])
        val gravZ = df.format(it.values[2])
        gravity_value.text = getString(R.string.gravity_value, gravX, gravY, gravZ)
    }

    private fun updateGyro(it: SensorEvent) {
        val gyroX = df.format(it.values[0])
        val gyroY = df.format(it.values[1])
        val gyroZ = df.format(it.values[2])
        gyroscope_value.text = getString(R.string.gyroscope_value, gyroX, gyroY, gyroZ)
    }


    private fun updateProximity(it: SensorEvent) {
        val prox = it.values[0].toString()
        proximity_value.text = getString(R.string.proximity_value, prox)
    }

    private fun updateMagnet(it: SensorEvent) {
        val magneticField = it.values[0]
        magnetic_value.text = getString(R.string.magnetic_value, df.format(magneticField))
    }

    private fun updateLight(it: SensorEvent) {
        val light = it.values[0]
        light_value.text = getString(R.string.light_value, light.toString())
    }

    private fun updatePressure(it: SensorEvent) {
        val millibarsOfPressure = it.values[0]
        val inchesMercury = millibarsOfPressure * CONVERT_MBAR_TO_INHG
        pressure_value.text = getString(R.string.pressure_value, df.format(inchesMercury))
    }

    companion object {
        const val CONVERT_MBAR_TO_INHG = 0.02953
    }
}