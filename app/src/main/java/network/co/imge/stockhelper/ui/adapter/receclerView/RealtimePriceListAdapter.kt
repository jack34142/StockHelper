package network.co.imge.stockhelper.ui.adapter.receclerView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.pojo.TwseResponse

class RealtimePriceListAdapter(val datas: MutableList<TwseResponse>):
        RecyclerView.Adapter<RealtimePriceListAdapter.ViewHolder>() {

    var context: Context? = null
    val latestPrice: MutableMap<String, Double> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.list_item_realtime_price, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]

        val stockId = data.stockId
        holder.text_stockId.text = stockId
        holder.text_stockName.text = data.stockName

        var nowPrice = data.nowPrice
        if (nowPrice == null)
            nowPrice = latestPrice[stockId]
        else
            latestPrice[stockId] = nowPrice

        if (nowPrice == null){
            holder.text_nowPrice.text = "-"
        }else{
            val diff = (nowPrice/data.yesterdayPrice - 1) * 100
            holder.text_nowPrice.text = String.format("%.2f (%+.2f%%)", nowPrice, diff)
        }
        holder.text_totalQty.text = data.totalQty.toString()

        holder.best5grid.layoutManager = object: GridLayoutManager(context, 4){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        holder.best5grid.adapter = Best5gridAdapter(data)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val text_stockId: TextView = itemView.findViewById(R.id.realtimePrice_stockId)
        val text_stockName: TextView = itemView.findViewById(R.id.realtimePrice_stockName)
        val text_nowPrice: TextView = itemView.findViewById(R.id.realtimePrice_nowPrice)
        val text_totalQty: TextView = itemView.findViewById(R.id.realtimePrice_totalQty)
        val best5grid: RecyclerView = itemView.findViewById(R.id.realtimePrice_best5grid)
        val foreView: View = itemView.findViewById(R.id.realtimePrice_foreView)

        init {
            foreView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            when(best5grid.visibility){
                View.VISIBLE -> best5grid.visibility = View.GONE
                else -> best5grid.visibility = View.VISIBLE
            }
        }
    }
}