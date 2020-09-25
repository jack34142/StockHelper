package network.co.imge.stockhelper.ui.adapter.receclerView

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.pojo.TwseResponse

class Best5gridAdapter(val data: TwseResponse): RecyclerView.Adapter<Best5gridAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val textView = TextView(context)

        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val scale = context.getResources().getDisplayMetrics().density
        val verticalPagging = (2 * scale).toInt()
        val horizontalPagging = (8 * scale).toInt()
        textView.setPadding(horizontalPagging, verticalPagging, horizontalPagging, verticalPagging)

        if (viewType == 0) textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        return ViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val column = position % 4
        val row = (position-column) / 4

        val textView = holder.itemView as TextView
        when(column){
            0 -> textView.text = data.best5purchaseQty.getOrNull(row)?.toString() ?: "-"
            1 -> textView.text = data.best5purchasePrice.getOrNull(row)?.toString() ?: "-"
            2 -> textView.text = data.best5sellPrice.getOrNull(row)?.toString() ?: "-"
            3 -> textView.text = data.best5sellQty.getOrNull(row)?.toString() ?: "-"
        }
    }

    override fun getItemViewType(position: Int): Int {
        val column = position % 4
        return if(column <= 1) 0 else 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}