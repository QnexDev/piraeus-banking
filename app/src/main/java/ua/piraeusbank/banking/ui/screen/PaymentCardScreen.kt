package ua.piraeusbank.banking.ui.screen

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.screen_bank_card.*
import kotlinx.android.synthetic.main.screen_main.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.StatementAction
import ua.piraeusbank.banking.ui.pm.PaymentCardPm
import ua.piraeusbank.banking.ui.screen.base.Screen

class PaymentCardScreen : Screen<PaymentCardPm>() {

    private lateinit var statementView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var statementAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = PaymentCardScreen()
    }

    override val screenLayout = R.layout.screen_bank_card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.main)
    }

    override fun providePresentationModel(): PaymentCardPm {
        return PaymentCardPm()
    }

    override fun onBindPresentationModel(pm: PaymentCardPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)
        statementAdapter = StatementAdapter(
            listOf(
                StatementAction(
                    StatementAction.TransferType.INCOMING,
                    "With a debit card (also known as a bank card, check card or some other description) when a cardholder makes a purchase",
                    "14.12.18 04:24"
                ),
                StatementAction(
                    StatementAction.TransferType.INCOMING,
                    "With a debit card (also known as a bank card, check card or some other description) when a cardholder makes a purchase",
                    "14.12.18 04:24"
                ),
                StatementAction(
                    StatementAction.TransferType.INCOMING,
                    "With a debit card (also known as a bank card, check card or some other description) when a cardholder makes a purchase",
                    "14.12.18 04:24"
                )
            ),
            R.layout.card_statement_action_layout
        )

        statementView = historyStatements.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = statementAdapter
        }
    }
}