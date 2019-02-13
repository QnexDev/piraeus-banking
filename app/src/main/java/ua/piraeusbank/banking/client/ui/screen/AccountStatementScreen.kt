package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_card_statement.*
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.service.client.getRxStatements
import ua.piraeusbank.banking.client.ui.screen.adapter.StatementAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.CurrentUserContext

class AccountStatementScreen : Screen<AccountStatementPm>() {

    private lateinit var statementView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var statementAdapter: StatementAdapter

    companion object {
        fun create() = AccountStatementScreen()
    }

    override val screenLayout = R.layout.screen_account_statement

    override fun providePresentationModel(): AccountStatementPm {
        return AccountStatementPm()
    }

    override fun onBindPresentationModel(pm: AccountStatementPm) {
        super.onBindPresentationModel(pm)

        Schedulers.io().scheduleDirect {
            val customerId = CurrentUserContext.customer.customerId!!
            App.component.customerRestClient.getRxStatements(customerId)
                .subscribeOn(Schedulers.io())
                .map {

                    statementAdapter.statementRecords.addAll(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    statementAdapter.notifyDataSetChanged()
                    moneyAmount.text = 9000.toString()
                }
        }

        viewManager = LinearLayoutManager(this.context)
        statementAdapter = StatementAdapter(
            ArrayList(),
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