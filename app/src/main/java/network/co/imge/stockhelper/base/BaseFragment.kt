package network.co.imge.stockhelper.base

import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.ui.dialog.LoadingDialog

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
}