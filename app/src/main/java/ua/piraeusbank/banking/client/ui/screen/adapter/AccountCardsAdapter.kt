package ua.piraeusbank.banking.client.ui.screen.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.bank_card_layout.view.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.PaymentCard

class AccountCardsAdapter(val cards: MutableList<PaymentCard>, private val cardLayout: Int) :
    RecyclerView.Adapter<AccountCardsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(cardLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardName.text = cards[position].name
        holder.cardNumber.text = "*${cards[position].binCode}"
        holder.cardType.setImageResource(
            when (cards[position].networkCode) {
                "VISA" -> R.drawable.ic_visa
                "MASTERCARD" -> R.drawable.ic_mastercard
                else -> throw IllegalArgumentException("Wrong card network code")
            }
        )
        holder.moneyAmount.text = cards[position].balance.amount.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardName: TextView = view.cardName
        val cardNumber: TextView = view.cardNumber
        val cardType: ImageView = view.cardType
        val moneyAmount: TextView = view.moneyAmount
    }
}