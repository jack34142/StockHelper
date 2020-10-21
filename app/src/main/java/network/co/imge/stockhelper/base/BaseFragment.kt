package network.co.imge.stockhelper.base

import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.ui.dialog.LoadingDialog
import network.co.imge.stockhelper.ui.dialog.MsgDialog

abstract class BaseFragment : Fragment(), IView {
    private val TAG: String = "BaseFragment"

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    override fun dispose() {}

    override fun showLoading() {
        LoadingDialog.showLoading(context!!)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun showMsg(msg: String, code: Int) {
        val msgDialog = MsgDialog(context!!)

        if (code > 0){
            msgDialog.setTitle("訊息")
        }else{
            msgDialog.setTitle("警告")
        }
        msgDialog.setMsg(msg)
        msgDialog.show()
    }
}