package ua.piraeusbank.banking.client.ui.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.bank_card_layout.view.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.PaymentCard

class CardSpinnerAdapter(context: Context,
                         private val viewResourceId: Int,
                         private val dropDownViewResourceId: Int,
                         val cards: MutableList<PaymentCard>) :
    ArrayAdapter<PaymentCard>(context, viewResourceId, cards) {

    override fun getCount(): Int {
        return cards.size
    }

    override fun getItem(position: Int): PaymentCard {
        return cards[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ) = getCustomView(convertView, parent, position, viewResourceId)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        getCustomView(convertView, parent, position, dropDownViewResourceId)

    private fun getCustomView(
        convertView: View?,
        parent: ViewGroup,
        position: Int,
        viewResourceId: Int
    ): View {
        val viewHolder: ViewHolder
        val view = if (convertView == null) {
            val innerView = LayoutInflater
                .from(parent.context)
                .inflate(viewResourceId, parent, false)
            viewHolder = ViewHolder(innerView)
            innerView.tag = viewHolder
            innerView
        } else {
            viewHolder = convertView.tag as ViewHolder
            convertView
        }

        viewHolder.cardName.text = cards[position].name
        viewHolder.cardNumber.text = "*${cards[position].binCode}"
        viewHolder.cardType?.setImageResource(
            when (cards[position].networkCode) {
                "VISA" -> R.drawable.ic_visa
                "MASTERCARD" -> R.drawable.ic_mastercard
                else -> throw IllegalArgumentException("Wrong card network code")
            }
        )
        viewHolder.moneyAmount.text = cards[position].balance.amount.toString()

        return view
    }

    private class ViewHolder(view: View) {
        val cardName: TextView = view.cardName
        val cardNumber: TextView = view.cardNumber
        val cardType: ImageView? = view.cardType
        val moneyAmount: TextView = view.moneyAmount
    }

}
