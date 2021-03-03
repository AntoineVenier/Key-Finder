package com.example.keytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selectdevice.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice        //add by me for using  val list : ArrayList<BluetoothDevice> = ArrayList()
import android.bluetooth.BluetoothSocket        //add by me for using var m_bluetoothSocket: BluetoothSocket? = null
import android.content.Context                  //add by me for using private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>()
import android.os.AsyncTask                     //add by me for using private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>()
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.AdapterView               //add by me for using AdapterView.OnItemClickListener


import java.util.UUID                           //add by me for using var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
import java.io.IOException                      //add by me for using catch (e: IOException)

import android.app.ProgressDialog               //add by me
import android.content.Intent
import android.util.Log                         //add by me



class SelectDeviceActivity:AppCompatActivity() {

    companion object{
        lateinit var bAdapter : BluetoothAdapter
        val list : ArrayList<BluetoothDevice> = ArrayList()
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var device_address: String
        lateinit var device_name: String
        lateinit var m_progress: ProgressDialog
        var m_isConnected: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectdevice)

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        if(bAdapter == null)
        {
            Toast.makeText(this,"No bluetooth available on this device",Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this,"Bluetooth is available on this device",Toast.LENGTH_LONG).show()
        }

        if(bAdapter.isEnabled)
        {
            displayDevices()
        }
        else
        {
            Toast.makeText(this,"Please enable your bluetooth",Toast.LENGTH_LONG).show()
        }

        /*select on device and connect it*/
        ListView_Devices.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            device_address = device.address
            device_name = device.toString()

            ConnectToDevice(this).execute()

            val intent = Intent(this, ChooseActionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun displayDevices()
    {
        var devices = bAdapter.bondedDevices


        for (device: BluetoothDevice in devices) {
            list.add(device)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        ListView_Devices.adapter = adapter
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null) {
                    val connect_device: BluetoothDevice = bAdapter.getRemoteDevice(device_name)
                    m_bluetoothSocket = connect_device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    bAdapter.cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }

    }
}