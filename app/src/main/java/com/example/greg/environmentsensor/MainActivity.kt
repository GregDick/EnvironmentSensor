package com.example.greg.environmentsensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private var pressure: Sensor? = null
    private var light: Sensor? = null
    private var magneticField: Sensor? = null
    val df = DecimalFormat("0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL)
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
            }
        }
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