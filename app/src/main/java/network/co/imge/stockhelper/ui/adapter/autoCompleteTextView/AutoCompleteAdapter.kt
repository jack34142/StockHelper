package network.co.imge.stockhelper.ui.adapter.autoCompleteTextView

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class AutoCompleteAdapter(
    context: Context,
    resource: Int,
    datas: List<String>
) : ArrayAdapter<String>(context, resource, datas), Filterable {

    val originDatas: List<String> = mutableListOf<String>().apply{addAll(datas)}

    val nameFilter: Filter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()

            if (constraint != null){
                val suggestions = mutableListOf<String>()
                originDatas.forEach {
                    if (it.startsWith(constraint)){
                        suggestions.add(0, it)
                    }else if(it.contains(constraint)){
                        suggestions.add(it)
                    }
                }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
            }
            return filterResults;
        }

        override fun publishResults(contraint: CharSequence?, results: FilterResults?) {
            if(results == null){
                return;
            }

            clear()
            if (results.count > 0){
                addAll(results.values as List<String>)
            }
            notifyDataSetChanged()
        }

    }

    override fun getFilter(): Filter {
        return nameFilter
    }
}