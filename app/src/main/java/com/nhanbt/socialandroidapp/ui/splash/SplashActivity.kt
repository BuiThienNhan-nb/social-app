package com.nhanbt.socialandroidapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nhanbt.socialandroidapp.ui.base.BaseActivity
import com.nhanbt.socialandroidapp.ui.home.MainActivity
import com.nhanbt.socialandroidapp.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        handlerThread = HandlerThread("inference")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        // Sets the condition to keep the splash screen visible
        splashScreen.setKeepOnScreenCondition {
            true
        }

        /*
            Fake preparing repository needed to start app
         */
        handler.postDelayed({
            startNextActivity()
        }, 2000L)
    }

    private fun startNextActivity() {
        /*
        Check condition to start next activity, for ex: current is logging to app
         */
        val intent: Intent = if (true) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
