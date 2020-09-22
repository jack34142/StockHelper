package network.co.imge.stockhelper.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.NoticeStock

class NoticeStockListAdapter(val stocks: List<NoticeStock>) : RecyclerView.Adapter<MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_notice_stock, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val stock = stocks[position]
        holder.text_stockId.text = stock.stockId
        holder.text_type.text = stock.type
        holder.text_price.text = stock.price.toString()
    }
}

class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text_stockId: TextView = itemView.findViewById(R.id.noticeStock_stockId)
    val text_type: TextView = itemView.findViewById(R.id.noticeStock_type)
    val text_price: TextView = itemView.findViewById(R.id.noticeStock_price)
}