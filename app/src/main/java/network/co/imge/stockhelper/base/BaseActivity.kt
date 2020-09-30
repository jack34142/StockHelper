package network.co.imge.stockhelper.base

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), IView {

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    override fun dispose() {}
}