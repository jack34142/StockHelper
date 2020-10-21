package network.co.imge.stockhelper.base

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import network.co.imge.stockhelper.ui.dialog.LoadingDialog
import network.co.imge.stockhelper.ui.dialog.MsgDialog

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

    override fun showMsg(msg: String, code: Int) {
        val msgDialog = MsgDialog(this)

        if (code > 0){
            msgDialog.setTitle("訊息")
        }else{
            msgDialog.setTitle("警告")
        }
        msgDialog.setMsg(msg)
        msgDialog.show()
    }
}