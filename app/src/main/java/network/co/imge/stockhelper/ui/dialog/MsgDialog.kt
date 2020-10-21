package network.co.imge.stockhelper.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import network.co.imge.stockhelper.R

class MsgDialog(context: Context) : Dialog(context) {
    private val text_title: TextView
    private val text_msg: TextView
    private val btn_ok: Button

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_msg, null)
        setContentView(v)

        text_title = v.findViewById(R.id.msg_title)
        text_msg = v.findViewById(R.id.msg_msg)
        btn_ok = v.findViewById(R.id.msg_ok)

        initListener()
    }

    fun initListener(){
        btn_ok.setOnClickListener {
            dismiss()
        }
    }

    override fun setTitle(title: CharSequence?) {
        text_title.text = title
    }

    override fun setTitle(titleId: Int) {
        text_title.setText(titleId)
    }

    fun setMsg(msg: String){
        text_msg.text = msg
    }

}