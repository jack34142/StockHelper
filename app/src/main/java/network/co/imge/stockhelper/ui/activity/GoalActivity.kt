package network.co.imge.stockhelper.ui.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import network.co.imge.stockhelper.R

class GoalActivity : AppCompatActivity() {

    private lateinit var text_msg: TextView
    private lateinit var btn_know: Button

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        setContentView(R.layout.activity_goal)
        initView()
        initData()
        initListener()

//        startVibrate()
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
    }

    fun initWindow(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

    fun initView(){
        btn_know = findViewById(R.id.goal_know)
        text_msg = findViewById(R.id.goal_msg)
    }

    fun initData(){
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        text_msg.text = intent.getStringExtra("msg")
    }

    fun initListener(){
        btn_know.setOnClickListener {
            finish()
        }
    }

    fun startVibrate(){
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val frequency = longArrayOf(500, 700)   // 停止時間, 震動時間
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(frequency, 0))
        } else {
            vibrator.vibrate(frequency, 0)
        }
    }
}