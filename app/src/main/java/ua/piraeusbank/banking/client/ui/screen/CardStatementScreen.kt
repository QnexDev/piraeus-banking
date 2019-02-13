package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_card_statement.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.StatementRecord
import ua.piraeusbank.banking.client.ui.screen.adapter.StatementAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class CardStatementScreen : Screen<CardStatementPm>() {

    private lateinit var statementView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var statementAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = CardStatementScreen()
    }

    override val screenLayout = R.layout.screen_card_statement

    override fun providePresentationModel(): CardStatementPm {
        return CardStatementPm()
    }

    override fun onBindPresentationModel(pm: CardStatementPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)
        statementAdapter = StatementAdapter(
            emptyList<StatementRecord>().toMutableList(),
            R.layout.statement_record_layout
        )

        statementView = historyStatements.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = statementAdapter
        }
    }
}

class CardStatementPm : ScreenPresentationModel()