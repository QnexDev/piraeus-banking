package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_card_statement.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.StatementRecord
import ua.piraeusbank.banking.client.ui.model.TransferType
import ua.piraeusbank.banking.client.ui.screen.adapter.StatementAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class AccountStatementScreen : Screen<AccountStatementPm>() {

    private lateinit var statementView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var statementAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = AccountStatementScreen()
    }

    override val screenLayout = R.layout.screen_account_statement

    override fun providePresentationModel(): AccountStatementPm {
        return AccountStatementPm()
    }

    override fun onBindPresentationModel(pm: AccountStatementPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)
        statementAdapter = StatementAdapter(
            listOf(
                StatementRecord(
                    "*4308",
                    "",
                    "Petrenko Grigory",
                    "",
                    TransferType.INCOMING,
                    "With a debit card (also known as a bank card, check card or some other description) when a cardholder makes a purchase",
                    "14.12.18 05:23",
                    "+200",
                    "UAH"
                ),
                StatementRecord(
                    "",
                    "*7408",
                    "",
                    "Pavlov Oleg",
                    TransferType.OUTGOING,
                    "With a debit card (also known as a bank card, check card or some other description) when a cardholder makes a purchase",
                    "15.12.18 04:11",
                    "-1000",
                    "UAH"

                ),
                StatementRecord(
                    "",
                    "*7408",
                    "",
                    "Pavlov Oleg",
                    TransferType.OUTGOING,
                    "With a debit card (also known as a bank card, check card or some other description) when a cardholder makes a purchase",
                    "16.12.18 14:54",
                    "-2000",
                    "UAH"
                )
            ),
            R.layout.statement_record_layout,
            true
        )

        statementView = historyStatements.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = statementAdapter
        }
    }
}

class AccountStatementPm : ScreenPresentationModel()