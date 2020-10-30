package network.co.imge.stockhelper.base

import androidx.appcompat.app.AppCompatActivity
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.ui.dialog.LoadingDialog
import network.co.imge.stockhelper.ui.dialog.MsgDialog

abstract class BaseActivity : AppCompatActivity(), IView {

    override fun showLoading() {
        LoadingDialog.showLoading(this)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun showMsg(msg: String, code: Int) {
        val msgDialog = MsgDialog(this)

        if (code > 0){
            msgDialog.setTitle(R.string.msg)
        }else{
            msgDialog.setTitle(R.string.alert)
        }
        msgDialog.setMsg(msg)
        msgDialog.show()
    }
}