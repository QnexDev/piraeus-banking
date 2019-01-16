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

class PaymentCardAdapter(private val cards: List<PaymentCard>, private val cardLayout: Int) :
    RecyclerView.Adapter<PaymentCardAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(cardLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardName.text = cards[position].cardName
        holder.cardNumber.text = "*${cards[position].cardNumber}"
        holder.cardType.setImageResource(
            when (cards[position].cardType) {
                PaymentCard.Type.VISA -> R.drawable.ic_visa
                PaymentCard.Type.MASTERCARD -> R.drawable.ic_mastercard
            }
        )
        holder.moneyAmount.text = cards[position].moneyAmount
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardName: TextView = view.cardName
        val cardNumber: TextView = view.cardNumber
        val cardType: ImageView = view.cardType
        val moneyAmount: TextView = view.moneyAmount
    }
}