package com.example.ybebrightapp.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.ybebrightapp.model.Province

class ProvinceAdapter(ctx: Context, @LayoutRes private val layoutResource: Int, private val list: List<Province>): ArrayAdapter<Province>(ctx, layoutResource, list) {

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val listFilter = arrayListOf<Province>()

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
            addAll(results!!.values as List<Province>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as Province).name
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
        view.text = list[position].name
        return view
    }
}