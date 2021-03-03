package com.example.keytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chooseaction.*

import com.example.keytracker.SelectDeviceActivity.Companion.m_bluetoothSocket
import java.io.IOException

class ChooseActionActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooseaction)


        button_map.setOnClickListener()
        {
            sendCommand("0")
        }


        button_buzzer.setOnClickListener()
        {
            sendCommand("1")
        }
    }

    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }
}