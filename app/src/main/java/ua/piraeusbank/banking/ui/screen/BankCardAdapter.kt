package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bank_card_layout.view.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.BankCard

class BankCardAdapter(private val cards: List<BankCard>, private val cardLayout: Int) :
    RecyclerView.Adapter<BankCardAdapter.ViewHolder>() {

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
                BankCard.Type.VISA -> R.drawable.ic_visa
                BankCard.Type.MASTERCARD -> R.drawable.ic_mastercard
            }
        )
        holder.moneyAmount.text = cards[position].moneyAmount
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardName = view.cardName!!
        val cardNumber = view.cardNumber!!
        val cardType = view.cardType!!
        val moneyAmount = view.moneyAmount!!
    }
}