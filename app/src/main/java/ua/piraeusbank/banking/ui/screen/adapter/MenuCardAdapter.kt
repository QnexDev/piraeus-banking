package ua.piraeusbank.banking.ui.screen.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.menu_card_layout.view.*

class MenuCardAdapter(private val menuItems: List<CardMenuItemViewConfig>, private val menuLayout: Int) :
    RecyclerView.Adapter<MenuCardAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(menuLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardMenuName.setText(menuItems[position].name)
        holder.cardMenuType.setImageResource(menuItems[position].icon)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardMenuName: TextView = view.cardMenuName
        val cardMenuType: ImageView = view.cardMenuType
    }
}

data class CardMenuItemViewConfig(val icon: Int, val name: Int)