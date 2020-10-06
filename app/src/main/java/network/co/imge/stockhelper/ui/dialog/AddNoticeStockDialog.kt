package network.co.imge.stockhelper.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.NoticeStock

class AddNoticeStockDialog(context: Context, stock: NoticeStock?,
                           val onComplete: (NoticeStock) -> Unit): Dialog(context) {
    private val TAG: String = "AddDialog"

    private val eText_stockId: EditText
    private val rGroup_type: RadioGroup
    private val eText_priceFrom: EditText
    private val eText_priceTo: EditText
    private val btn_cancel: Button
    private val btn_commit: Button

    private val stock: NoticeStock = stock ?: NoticeStock()

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_add_notice_stock, null)
        setContentView(v)

        eText_stockId = v.findViewById(R.id.addNoticeStock_stockId)
        rGroup_type = v.findViewById(R.id.addNoticeStock_type)
        eText_priceFrom = v.findViewById(R.id.addNoticeStock_priceFrom)
        eText_priceTo = v.findViewById(R.id.addNoticeStock_priceTo)
        btn_cancel = v.findViewById(R.id.addNoticeStock_cancel)
        btn_commit = v.findViewById(R.id.addNoticeStock_complete)

        initListener()
        if (stock != null) initData(stock)
    }

    private fun initListener(){
        btn_cancel.setOnClickListener{
            dismiss()
        }
        btn_commit.setOnClickListener{
            val stockId = eText_stockId.text.toString()
            val priceFrom = eText_priceFrom.text.toString()
            val priceTo = eText_priceTo.text.toString()
            val type = when(rGroup_type.checkedRadioButtonId){
                R.id.addNoticeStock_tse -> "tse"
                R.id.addNoticeStock_otc -> "otc"
                else -> null
            }

            if (stockId.isEmpty() || type == null || priceFrom.isEmpty() || priceTo.isEmpty()){
                Toast.makeText(context,
                    context.getString(R.string.data_not_complete), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dismiss()
            stock.stockId = stockId
            stock.type = type
            stock.priceFrom = priceFrom.toDouble()
            stock.priceTo = priceTo.toDouble()
            onComplete(stock)
        }
    }

    private fun initData(stock: NoticeStock) {
        eText_stockId.setText(stock.stockId)
        eText_stockId.isEnabled = false

        eText_priceFrom.setText(stock.priceFrom.toString())
        eText_priceTo.setText(stock.priceTo.toString())

        when(stock.type){
            "tse" -> rGroup_type.check(R.id.addNoticeStock_tse)
            "otc" -> rGroup_type.check(R.id.addNoticeStock_otc)
        }
    }
}