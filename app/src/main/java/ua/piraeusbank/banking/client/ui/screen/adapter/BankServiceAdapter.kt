package ua.piraeusbank.banking.client.ui.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.bank_service_view_item.view.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.BankServiceKind

class BankServiceAdapter(private val bankServices: List<BankServiceViewConfig>,
                         private val onSelectService: (View, BankServiceViewConfig) -> Unit) : BaseAdapter() {

    override fun getCount(): Int {
        return bankServices.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        class OnSelectServiceListener:  (View) -> Unit {
            override fun invoke(view: View) {
                onSelectService.invoke(view, bankServices[position])
            }
        }

        val viewHolder: ViewHolder
        val view = if (convertView == null) {
            val innerView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.bank_service_view_item, parent, false)
            viewHolder = ViewHolder(innerView)
            innerView.setOnClickListener(OnSelectServiceListener())
            innerView.tag = viewHolder
            innerView
        } else {
            viewHolder = convertView.tag as ViewHolder
            convertView
        }

        viewHolder.text.setText(bankServices[position].name)
        viewHolder.icon.setImageResource(bankServices[position].icon)

        return view
    }

    private class ViewHolder(view: View) {
        val text: TextView = view.mainServiceText
        val icon: ImageView = view.mainServiceIcon
    }

}

data class BankServiceViewConfig(val bankServiceKind: BankServiceKind, val icon: Int, val name: Int)

