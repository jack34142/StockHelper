package network.co.imge.stockhelper.ui.adapter.receclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.NoticeStock

class NoticeStockListAdapter(val stocks: List<NoticeStock>,
                             val onEdit: (Int, NoticeStock) -> Unit,
                             val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<NoticeStockListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_notice_stock, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = stocks[position]
        holder.text_stockId.text = stock.stockId
        holder.text_type.text = stock.type
        holder.text_price.text = stock.price.toString()
        holder.itemView.setOnClickListener{
            onEdit(position, stock)
        }
        holder.iBtn_delete.setOnClickListener{
            onDelete(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_stockId: TextView = itemView.findViewById(R.id.noticeStock_stockId)
        val text_type: TextView = itemView.findViewById(R.id.noticeStock_type)
        val text_price: TextView = itemView.findViewById(R.id.noticeStock_price)
        val iBtn_delete: ImageButton = itemView.findViewById(R.id.noticeStock_delete)
    }
}