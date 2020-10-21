package network.co.imge.stockhelper.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.ui.adapter.autoCompleteTextView.AutoCompleteAdapter

class AddNoticeStockDialog(context: Context, stock: NoticeStock?,
                           val onComplete: (NoticeStock) -> Unit): Dialog(context) {
    private val TAG: String = "AddDialog"

    private val eText_stockId: AutoCompleteTextView
    private val eText_priceFrom: EditText
    private val eText_priceTo: EditText
    private val btn_cancel: Button
    private val btn_commit: Button

    private val stock: NoticeStock = stock ?: NoticeStock()

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_add_notice_stock, null)
        setContentView(v)

        eText_stockId = v.findViewById(R.id.addNoticeStock_stockId)
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

            val priceFrom: Double
            val priceTo: Double
            if (eText_priceFrom.text.toString().isEmpty()){
                priceFrom = 0.0
            }else{
                priceFrom = eText_priceFrom.text.toString().toDouble()
            }
            if (eText_priceTo.text.toString().isEmpty()){
                priceTo = priceFrom
            }else{
                priceTo = eText_priceTo.text.toString().toDouble()
            }

            if (stockId.isEmpty() || priceTo == 0.0){
                Toast.makeText(context,
                    context.getString(R.string.data_not_complete), Toast.LENGTH_SHORT).show()
            }else {
                if (priceFrom > priceTo){
                    Toast.makeText(context,
                        context.getString(R.string.price_range_error), Toast.LENGTH_SHORT).show()
                }else{
                    dismiss()
                    stock.stockId = stockId
                    stock.priceFrom = priceFrom
                    stock.priceTo = priceTo
                    onComplete(stock)
                }
            }
        }
    }

    private fun initData(stock: NoticeStock) {
        eText_stockId.setText(stock.stockId)
        eText_stockId.isEnabled = false
        eText_stockId.setTextColor(ContextCompat.getColor(context, R.color.colorDisable))

        eText_priceFrom.setText(stock.priceFrom.toString())
        eText_priceTo.setText(stock.priceTo.toString())
    }

    fun setDataList(list: List<String>){
        eText_stockId.threshold = 1
        eText_stockId.setAdapter(AutoCompleteAdapter(context, android.R.layout.simple_spinner_dropdown_item, list))
    }
}