package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bank_card_display.view.*
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.pm.MainPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class MainScreen : Screen<MainPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = MainScreen()
    }

    override val screenLayout = R.layout.screen_main

    override fun providePresentationModel(): MainPm {
        return MainPm()
    }

    override fun onBindPresentationModel(pm: MainPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)
        bankCardAdapter = BankCardAdapter(
            listOf(
                BankCard("Payment bank card", BankCard.Type.MASTERCARD, "9 950 UAH"),
                BankCard("Credit bank card", BankCard.Type.VISA, "7 680 UAH"),
                BankCard("Credit bank card", BankCard.Type.VISA, "10 150 UAH")
            )
        )

        bankCardsView = bankCards.apply {
            //            setHasFixedSize(true)

            layoutManager = viewManager

            // specify an bankCardAdapter (see also next example)
            adapter = bankCardAdapter

        }
    }

    class BankCardAdapter(private val cards: List<BankCard>) :
        RecyclerView.Adapter<BankCardAdapter.ViewHolder>() {

        override fun getItemCount(): Int {
            return cards.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.bank_card_display, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.cardName.text = cards[position].cardName
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
            val cardType = view.cardType!!
            val moneyAmount = view.moneyAmount!!
        }
    }

    data class BankCard(val cardName: String, val cardType: Type, val moneyAmount: String) {
        enum class Type {
            VISA, MASTERCARD
        }
    }

}