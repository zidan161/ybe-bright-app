package com.zidan.ybebrightapp.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.zidan.ybebrightapp.model.City

class CityAdapter(ctx: Context, @LayoutRes private val layoutResource: Int, private val list: List<City>): ArrayAdapter<City>(ctx, layoutResource, list) {

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val listFilter = arrayListOf<City>()

            if (constraint == null || constraint.isEmpty()) {
                listFilter.addAll(list)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in list) {
                    if (item.name.lowercase().contains(filterPattern)) {
                        listFilter.add(item)
                    }
                }
            }

            results.values = listFilter
            results.count = listFilter.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results!!.values as List<City>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any): CharSequence {
            return if ((resultValue as City).type == "Kabupaten") {
                "Kab. ${resultValue.name}"
            } else {
                resultValue.name
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = if (list[position].type == "Kabupaten") {
            "Kab. ${list[position].name}"
        } else {
             list[position].name
        }
        return view
    }
}