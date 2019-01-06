package ua.piraeusbank.banking.ui.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.bank_service_view_item.view.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.BankServiceKind

class BankServiceAdapter(private val bankServices: List<BankServiceViewConfig>,
                         private val itemClickListener: (View, BankServiceViewConfig) -> Unit) : BaseAdapter() {

    override fun getCount(): Int {
        return bankServices.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        class OnClickListener:  (View) -> Unit {
            override fun invoke(view: View) {
                itemClickListener.invoke(view, bankServices[position])
            }
        }

        val viewHolder: ViewHolder
        val bankServiceView = if (convertView == null) {
            val innerBankServiceView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.bank_service_view_item, parent, false)
            viewHolder = ViewHolder(innerBankServiceView)
            innerBankServiceView.tag = viewHolder
            innerBankServiceView
        } else {
            viewHolder = convertView.tag as ViewHolder
            convertView
        }

        bankServiceView.setOnClickListener(OnClickListener())
        viewHolder.text.setText(bankServices[position].name)
        viewHolder.icon.setImageResource(bankServices[position].icon)

        return bankServiceView
    }

    private class ViewHolder(view: View) {
        val text: TextView = view.mainServiceText
        val icon: ImageView = view.mainServiceIcon
    }

}

data class BankServiceViewConfig(val bankServiceKindScreen: BankServiceKind, val icon: Int, val name: Int)

