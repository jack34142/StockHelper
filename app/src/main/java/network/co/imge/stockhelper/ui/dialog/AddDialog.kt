package network.co.imge.stockhelper.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.NoticeStock

class AddDialog(context: Context,
                val onCommit: (NoticeStock) -> Unit): Dialog(context) {
    private val TAG: String = "AddDialog"

    private val eText_stockId: EditText
    private val rGroup_type: RadioGroup
    private val eText_price: EditText
    private val btn_commit: Button

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_add, null)
        setContentView(v)


        eText_stockId = v.findViewById(R.id.add_stockId)
        rGroup_type = v.findViewById(R.id.add_type)
        eText_price = v.findViewById(R.id.add_price)
        btn_commit = v.findViewById(R.id.add_commit)

        initListener()
    }

    private fun initListener(){
        btn_commit.setOnClickListener{
            val stockId = eText_stockId.text.toString()
            val type = when(rGroup_type.checkedRadioButtonId){
                R.id.add_tse -> "tse"
                R.id.add_otc -> "otc"
                else -> null
            }
            val price = eText_price.text.toString()

            if (stockId.isEmpty() || type == null || price.isEmpty()){
                Toast.makeText(context, "資料未完全", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dismiss()
            val stock = NoticeStock(stockId, type, price.toDouble())
            onCommit(stock)
        }
    }
}