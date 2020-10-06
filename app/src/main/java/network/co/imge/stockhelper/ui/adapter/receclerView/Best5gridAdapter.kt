package network.co.imge.stockhelper.ui.adapter.receclerView

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.TwseResponse

class Best5gridAdapter(val data: TwseResponse): RecyclerView.Adapter<Best5gridAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.grid_item_best5, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val column = position % 2
        val row = (position-column) / 2

        val yesterdayPrice = data.yesterdayPrice
        val price: Double?
        val qty: Int?

        if(column == 0){
            price = data.best5purchasePrice.getOrNull(row)
            qty = data.best5purchaseQty.getOrNull(row)

            holder.left.gravity = Gravity.END
            holder.right.gravity = Gravity.END
            if (price == null){
                holder.right.text = "-"
            }else if (price == 0.0){
                holder.right.text = context.getString(R.string.at_the_market)
                holder.left.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.right.setTextColor(ContextCompat.getColor(context, R.color.red))
            }else{
                holder.right.text = price.toString()
                if (price > yesterdayPrice){
                    holder.left.setTextColor(ContextCompat.getColor(context, R.color.red))
                    holder.right.setTextColor(ContextCompat.getColor(context, R.color.red))
                }else if (price < yesterdayPrice){
                    holder.left.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.right.setTextColor(ContextCompat.getColor(context, R.color.green))
                }else {
                    holder.left.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                    holder.right.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                }
            }
            if (qty == null){
                holder.left.text = "-"
            }else{
                holder.left.text = qty.toString()
            }
        }else{
            price = data.best5sellPrice.getOrNull(row)
            qty = data.best5sellQty.getOrNull(row)

            if (price == null){
                holder.left.text = "-"
            }else if (price == 0.0){
                holder.left.text = context.getString(R.string.at_the_market)
                holder.left.setTextColor(ContextCompat.getColor(context, R.color.green))
                holder.right.setTextColor(ContextCompat.getColor(context, R.color.green))
            }else{
                holder.left.text = price.toString()
                if (price > yesterdayPrice){
                    holder.left.setTextColor(ContextCompat.getColor(context, R.color.red))
                    holder.right.setTextColor(ContextCompat.getColor(context, R.color.red))
                }else if (price < yesterdayPrice){
                    holder.left.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.right.setTextColor(ContextCompat.getColor(context, R.color.green))
                }else {
                    holder.left.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                    holder.right.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                }
            }
            if (qty == null){
                holder.right.text = "-"
            }else{
                holder.right.text = qty.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val left: TextView = itemView.findViewById(R.id.brst5_left)
        val right: TextView = itemView.findViewById(R.id.brst5_right)
    }
}