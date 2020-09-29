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
    private val rGroup_method: RadioGroup
    private val eText_price: EditText
    private val btn_cancel: Button
    private val btn_commit: Button

    private val stock: NoticeStock = stock ?: NoticeStock()

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_add_notice_stock, null)
        setContentView(v)

        eText_stockId = v.findViewById(R.id.addNoticeStock_stockId)
        rGroup_type = v.findViewById(R.id.addNoticeStock_type)
        rGroup_method = v.findViewById(R.id.addNoticeStock_method)
        eText_price = v.findViewById(R.id.addNoticeStock_price)
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
            val type = when(rGroup_type.checkedRadioButtonId){
                R.id.addNoticeStock_tse -> "tse"
                R.id.addNoticeStock_otc -> "otc"
                else -> null
            }
            val price = when(rGroup_method.checkedRadioButtonId){
                R.id.addNoticeStock_lower -> eText_price.text.toString()
                R.id.addNoticeStock_higher -> "-" + eText_price.text.toString()
                else -> ""
            }

            if (stockId.isEmpty() || type == null || price.isEmpty()){
                Toast.makeText(context,
                    context.getString(R.string.data_not_complete), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dismiss()
            stock.stockId = stockId
            stock.type = type
            stock.price = price.toDouble()
            onComplete(stock)
        }
    }

    private fun initData(stock: NoticeStock) {
        eText_stockId.setText(stock.stockId)
        when(stock.type){
            "tse" -> rGroup_type.check(R.id.addNoticeStock_tse)
            "otc" -> rGroup_type.check(R.id.addNoticeStock_otc)
        }

        val price = stock.price!!
        if(price > 0){
            rGroup_method.check(R.id.addNoticeStock_lower)
        }else{
            rGroup_method.check(R.id.addNoticeStock_higher)
        }
        eText_price.setText(price.toString())
    }
}