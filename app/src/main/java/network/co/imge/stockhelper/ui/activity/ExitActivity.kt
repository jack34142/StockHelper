package network.co.imge.stockhelper.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ExitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishAffinity()
    }
}