package network.co.imge.stockhelper.base

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import network.co.imge.stockhelper.ui.dialog.LoadingDialog

abstract class BaseActivity : AppCompatActivity(), IView {

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    override fun dispose() {}

    override fun showLoading() {
        LoadingDialog.showLoading(this)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }
}