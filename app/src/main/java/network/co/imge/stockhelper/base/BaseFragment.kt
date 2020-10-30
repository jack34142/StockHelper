package network.co.imge.stockhelper.base

import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.ui.dialog.LoadingDialog
import network.co.imge.stockhelper.ui.dialog.MsgDialog

abstract class BaseFragment : Fragment(), IView {

    override fun showLoading() {
        LoadingDialog.showLoading(context!!)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun showMsg(msg: String, code: Int) {
        val msgDialog = MsgDialog(context!!)

        if (code > 0){
            msgDialog.setTitle(R.string.msg)
        }else{
            msgDialog.setTitle(R.string.alert)
        }
        msgDialog.setMsg(msg)
        msgDialog.show()
    }
}