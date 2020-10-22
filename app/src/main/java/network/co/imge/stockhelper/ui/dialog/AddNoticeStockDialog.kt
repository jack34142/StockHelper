package network.co.imge.stockhelper.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
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
    private val btn_clear: ImageButton

    private val stock: NoticeStock = stock ?: NoticeStock()

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_add_notice_stock, null)
        setContentView(v)

        eText_stockId = v.findViewById(R.id.addNoticeStock_stockId)
        eText_priceFrom = v.findViewById(R.id.addNoticeStock_priceFrom)
        eText_priceTo = v.findViewById(R.id.addNoticeStock_priceTo)
        btn_cancel = v.findViewById(R.id.addNoticeStock_cancel)
        btn_commit = v.findViewById(R.id.addNoticeStock_complete)
        btn_clear = v.findViewById(R.id.addNoticeStock_clear)

        if (stock != null) initData(stock)
        initListener()
    }

    private fun initListener(){
        eText_stockId.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                stock.type = null
            }
        })
        btn_clear.setOnClickListener {
            eText_stockId.text.clear()
        }
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
                }else if(stock.type == null){
                    Toast.makeText(context,
                        "無效的代號", Toast.LENGTH_SHORT).show()
                }else{
                    dismiss()
                    stock.stockId = stockId.split(" ")[0]
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

        btn_clear.visibility = ViewGroup.GONE
    }

    fun setDataList(typeMap: Map<String, String>){
        eText_stockId.threshold = 1
        val adapter = AutoCompleteAdapter(context, android.R.layout.simple_spinner_dropdown_item, typeMap.keys.toList())
        eText_stockId.setAdapter(adapter)

        eText_stockId.setOnItemClickListener { adapterView, view, i, l ->
            stock.type = typeMap[eText_stockId.text.toString()]
        }

        eText_stockId.setOnDismissListener {
            if (stock.type == null && eText_stockId.text.isNotEmpty() && adapter.count > 0){
                eText_stockId.setAdapter(null)
                eText_stockId.setText(adapter.getItem(0))
                eText_stockId.setAdapter(adapter)

                stock.type = typeMap[eText_stockId.text.toString()]
            }
        }
    }
}