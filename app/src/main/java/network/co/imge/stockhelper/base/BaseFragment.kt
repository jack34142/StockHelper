package network.co.imge.stockhelper.base

import android.content.Intent
import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.ui.activity.GoalActivity


abstract class BaseFragment : Fragment(), IView {
    private val TAG: String = "BaseFragment"

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    override fun dispose() {}
}