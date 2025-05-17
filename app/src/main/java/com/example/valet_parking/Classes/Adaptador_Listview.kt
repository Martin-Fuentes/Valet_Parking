package com.example.valet_parking.Classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.valet_parking.DataClasses.Data_ConductorVehiculo
import com.example.valet_parking.R

class Adaptador_Listview(private val context: Context, private var originalList: List<Data_ConductorVehiculo>) : BaseAdapter(),
    Filterable {

    private var filteredList = originalList.toMutableList()

    override fun getCount(): Int = filteredList.size

    override fun getItem(position: Int): Data_ConductorVehiculo  = filteredList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = filteredList[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_listview, parent, false)

        view.findViewById<TextView>(R.id.txtConductor).text = "${item.nombreConductor} | ${item.cedulaConductor}"
        view.findViewById<TextView>(R.id.txtVehiculo).text = "${item.placaVehiculo} | ${item.marcaVehiculo} | ${item.modeloVehiculo} | ${item.colorVehiculo} | ${item.tipoVehiculo}"

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                val result = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.nombreConductor.lowercase().contains(query) || it.placaVehiculo.lowercase().contains(query)
                    }
                }

                return FilterResults().apply { values = result }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<Data_ConductorVehiculo>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newList: List<Data_ConductorVehiculo>) {
        originalList = newList
        filteredList = newList.toMutableList()
        notifyDataSetChanged()
    }
    fun mostrarSolo(item: Data_ConductorVehiculo) {
        filteredList.clear()
        filteredList.add(item)
        notifyDataSetChanged()
    }
}