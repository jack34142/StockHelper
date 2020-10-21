package network.co.imge.stockhelper.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import network.co.imge.stockhelper.R

class LoadingDialog private constructor(context: Context) : Dialog(context) {

    companion object{
        private var dialog: LoadingDialog? = null

        fun showLoading(context: Context){
            if (dialog == null){
                dialog = LoadingDialog(context)
                dialog!!.show()
            }
        }

        fun hideLoading(){
            dialog?.dismiss()
            dialog = null
        }
    }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        setContentView(v)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }
}