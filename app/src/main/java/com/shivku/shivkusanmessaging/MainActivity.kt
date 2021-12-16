package com.shivku.shivkusanmessaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


class MainActivity : AppCompatActivity() {
    lateinit var simpleButton1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleButton1 = findViewById<Button>(R.id.simpleButton1)

        simpleButton1.setOnClickListener {
            Log.w(TAG, "Subscribing to OA-25")

            Firebase.messaging.subscribeToTopic("oa-25")
                .addOnCompleteListener {
                    Log.w(TAG, "Subscribed successfully to OA-25")
                }

            Toast.makeText(applicationContext, "Simple Button 1", Toast.LENGTH_LONG)
                .show() //display the text of button1
        }


        var helloTextView = findViewById<TextView>(R.id.text_view_id)
        helloTextView.text = "Rar"

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = "FCM Token $token"
                Log.d(TAG, msg)

                val helloTextView = findViewById<TextView>(R.id.text_view_id_token)
                helloTextView.text = token
            })

        for (i in 1 until 20)
            Firebase.messaging.subscribeToTopic("oa-$i")
                .addOnCompleteListener { task ->
                    var msg = "Subscribed in $i!"
                    if (!task.isSuccessful) {
                        msg = "Could not subscribe! in $TAG"
                    }
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }

    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter(MyFirebaseMessagingService.REQUEST_ACCEPT)
        )
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.extras!!.toString()
            Log.d("receiver", "Got message: $message")
            var helloTextView = findViewById<TextView>(R.id.text_view_id)
            helloTextView.text = message
        }
    }

    private val mTokenReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.extras!!.toString()
            Log.d("receiver", "Got token: $message")
            var helloTextView = findViewById<TextView>(R.id.text_view_id_token)
            helloTextView.text = message
        }
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}