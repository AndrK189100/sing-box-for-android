package io.nekohasekai.sfa.automation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.nekohasekai.sfa.bg.BoxService
import io.nekohasekai.sfa.database.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AutomationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return

        val token = intent.getStringExtra(EXTRA_TOKEN)
        if (token != "andrka-sing-box") {
            Log.w(TAG, "Rejected automation intent: invalid token")
            return
        }

        when (action) {
            ACTION_START -> {
                Log.i(TAG, "START requested")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        Settings.startedByUser = true
                        BoxService.start()
                    } catch (e: Throwable) {
                        Log.e(TAG, "Failed to start service", e)
                    }
                }
            }

            ACTION_STOP -> {
                Log.i(TAG, "STOP requested")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        BoxService.stop()
                    } catch (e: Throwable) {
                        Log.e(TAG, "Failed to stop service", e)
                    }
                }
            }

            ACTION_TOGGLE -> {
                Log.i(TAG, "TOGGLE requested")
                // not need
            }

            else -> {
                Log.w(TAG, "Unknown action: $action")
            }
        }
    }

    companion object {
        private const val TAG = "SFA-Automation"

        const val ACTION_START = "com.andrka.sfa.intent.START"
        const val ACTION_STOP = "com.andrka.sfa.intent.STOP"
        const val ACTION_TOGGLE = "com.andrka.sfa.intent.TOGGLE"

        const val EXTRA_TOKEN = "token"
    }
}
