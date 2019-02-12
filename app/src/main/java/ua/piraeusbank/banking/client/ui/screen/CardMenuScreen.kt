package ua.piraeusbank.banking.client.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_card_menu.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.CardMenuKind.*
import ua.piraeusbank.banking.client.ui.navigation.SelectCardMenuMessage
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuAdapter
import ua.piraeusbank.banking.client.ui.screen.adapter.CardMenuPreferences
import ua.piraeusbank.banking.client.ui.screen.base.Screen
import ua.piraeusbank.banking.client.ui.screen.base.ScreenPresentationModel

class CardMenuScreen : Screen<CardMenuPm>() {

    private lateinit var cardMenuItemsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var cardMenuAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = CardMenuScreen()
        val CARD_MENU_ITEM_VIEW_CONFIGS = listOf(
            CardMenuPreferences(R.drawable.ic_block_card, R.string.menu_card_block_card, BLOCK_CARD),
            CardMenuPreferences(R.drawable.ic_unblock_card, R.string.menu_card_unblock_card, UNBLOCK_CARD),
            CardMenuPreferences(R.drawable.ic_add_card, R.string.menu_card_order_card, ORDER_CARD),
            CardMenuPreferences(R.drawable.ic_secutity_code, R.string.menu_card_get_security_code, GET_SECURITY_CODE,
                R.string.menu_card_get_security_code_descr),
            CardMenuPreferences(R.drawable.ic_pin_code, R.string.menu_card_change_pin, CHANGE_PIN_CODE,
                R.string.menu_card_change_pin_descr),
            CardMenuPreferences(R.drawable.ic_close_card, R.string.menu_card_close_card, CLOSE_CARD,
                R.string.menu_card_close_card_descr)
        )
    }

    override val screenLayout = R.layout.screen_card_menu

    override fun providePresentationModel(): CardMenuPm {
        return CardMenuPm()
    }

    override fun onBindPresentationModel(pm: CardMenuPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)

        cardMenuAdapter = CardMenuAdapter(
            CARD_MENU_ITEM_VIEW_CONFIGS,
            R.layout.menu_card_layout,
            onSelect = { _, preferences -> pm.selectMenuAction.consumer.accept(preferences) }
        )

        cardMenuItemsView = cardMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = cardMenuAdapter
        }
    }
}

class CardMenuPm : ScreenPresentationModel() {

    val selectMenuAction = Action<CardMenuPreferences>()

    override fun onCreate() {
        super.onCreate()


        selectMenuAction.observable
            .subscribe { sendMessage(SelectCardMenuMessage(it)) }
            .untilDestroy()
    }
}