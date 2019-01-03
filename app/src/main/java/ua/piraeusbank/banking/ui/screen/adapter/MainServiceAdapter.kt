package ua.piraeusbank.banking.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.main_service_view_item.view.*
import ua.piraeusbank.banking.R

class MainServiceAdapter(private val services: List<ServiceViewResources>) : BaseAdapter() {

    override fun getCount(): Int {
        return services.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var serviceView = convertView

        val viewHolder: ViewHolder
        if (serviceView == null) {
            serviceView = LayoutInflater.from(parent.context).inflate(R.layout.main_service_view_item, parent, false)
            viewHolder = ViewHolder(serviceView)
            serviceView.tag = viewHolder
        } else {
            viewHolder = serviceView.tag as ViewHolder
        }

        viewHolder.text.setText(services[position].name)
        viewHolder.icon.setImageResource(services[position].icon)

        return serviceView!!
    }

    class ViewHolder(view: View) {
        val text: TextView = view.mainServiceText
        val icon: ImageView = view.mainServiceIcon
    }

}

data class ServiceViewResources(val layout: Int, val icon: Int, val name: Int)

