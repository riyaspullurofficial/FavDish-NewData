package com.riyaspullur.favdish

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.riyaspullur.favdish.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashBind=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBind.root)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        var animSplash=AnimationUtils.loadAnimation(this,R.anim.splash_animation)
        tv_app_name.animation=animSplash

        animSplash.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                //asdas
            }

            override fun onAnimationEnd(animation: Animation?) {
              Handler(Looper.getMainLooper()).postDelayed({
                  startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                  finish()
              },3000)
            }

            override fun onAnimationRepeat(animation: Animation?) {
               //sdas
            }

        })

    }
}