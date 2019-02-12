package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.fasterxml.jackson.core.type.TypeReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_account.*
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.service.client.getPaymentCardsByCustomerId
import ua.piraeusbank.banking.client.ui.model.PaymentCard
import ua.piraeusbank.banking.client.ui.screen.adapter.AccountCardsAdapter
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel
import ua.piraeusbank.banking.client.util.CurrentUserContext

class AccountScreen : Screen<AccountPm>() {

    private lateinit var bankCardsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var bankCardAdapter: AccountCardsAdapter


    companion object {
        fun create() = AccountScreen()
    }

    override val screenLayout = R.layout.screen_account

    override fun providePresentationModel(): AccountPm {
        return AccountPm()
    }

    override fun onBindPresentationModel(pm: AccountPm) {
        super.onBindPresentationModel(pm)

        val customerId = CurrentUserContext.customer.customerId!!
        App.component.cardRestClient.getPaymentCardsByCustomerId(customerId)
            .subscribeOn(Schedulers.io())
            .map {
                val paymentCards: List<PaymentCard> =
                    App.component.objectMapper.readValue<List<PaymentCard>>(it.string(), object : TypeReference<List<PaymentCard>>() {})

                bankCardAdapter.cards.addAll(paymentCards)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                bankCardAdapter.notifyDataSetChanged()
            }

        viewManager = LinearLayoutManager(this.context)
        bankCardAdapter = AccountCardsAdapter(
            ArrayList(),
            R.layout.full_bank_card_layout
        )

        bankCardsView = allBankCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bankCardAdapter
        }
    }
}

class AccountPm : ScreenPresentationModel()