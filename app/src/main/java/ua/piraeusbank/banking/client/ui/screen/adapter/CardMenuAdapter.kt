package ua.piraeusbank.banking.client.ui.screen.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.menu_card_layout.view.*
import ua.piraeusbank.banking.client.ui.model.CardMenuKind
import java.io.Serializable

class CardMenuAdapter(
    private val menuItems: List<CardMenuPreferences>,
    private val menuLayout: Int,
    private val onSelect: (View, CardMenuPreferences) -> Unit) :
    RecyclerView.Adapter<CardMenuAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(menuLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardMenuPreferences = menuItems[position]

        class OnSelectListener:  (View) -> Unit {
            override fun invoke(view: View) {
                onSelect.invoke(view, cardMenuPreferences)
            }
        }
        holder.view.setOnClickListener(OnSelectListener())
        holder.cardMenuName.setText(cardMenuPreferences.name)
        holder.cardMenuType.setImageResource(cardMenuPreferences.icon)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cardMenuName: TextView = view.cardMenuName
        val cardMenuType: ImageView = view.cardMenuType
    }
}

data class CardMenuPreferences(val icon: Int,
                               val name: Int,
                               val kind: CardMenuKind,
                               val description: Int? = null): Serializable