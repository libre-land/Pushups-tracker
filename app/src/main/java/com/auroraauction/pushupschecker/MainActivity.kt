package com.auroraauction.pushupschecker

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

private lateinit var sensorManager: SensorManager
private var proximity: Sensor? = null
private var count = -1

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        // Make sure that device has sensor proximity feature
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY)) {
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        } else {
            Toast.makeText(
                    this,
                    "Unfortunately, your device does not support proximity sensor",
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val tvText = findViewById<TextView>(R.id.tvText)
        var distance = event!!.values[0]

        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY && distance != 0f) {
            count++
            tvText.text = count.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        proximity?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        // Make sure that sensor register listener switches off if not using
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}