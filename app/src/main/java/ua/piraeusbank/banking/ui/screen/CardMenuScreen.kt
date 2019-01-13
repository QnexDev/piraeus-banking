package ua.piraeusbank.banking.ui.screen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.screen_card_menu.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.screen.adapter.CardMenuItemViewConfig
import ua.piraeusbank.banking.ui.screen.adapter.MenuCardAdapter
import ua.piraeusbank.banking.ui.screen.base.Screen
import ua.piraeusbank.banking.ui.screen.base.ScreenPresentationModel

class CardMenuScreen : Screen<CardMenuPm>() {

    private lateinit var cardMenuItemsView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var cardMenuAdapter: RecyclerView.Adapter<*>

    companion object {
        fun create() = CardMenuScreen()
        val CARD_MENU_ITEM_VIEW_CONFIGS = listOf(
            CardMenuItemViewConfig(R.drawable.ic_block_card, R.string.menu_card_block_card),
            CardMenuItemViewConfig(R.drawable.ic_unblock_card, R.string.menu_card_unblock_card),
            CardMenuItemViewConfig(R.drawable.ic_add_card, R.string.menu_card_add_card),
            CardMenuItemViewConfig(R.drawable.ic_close_card, R.string.menu_card_close_card)
        )
    }

    override val screenLayout = R.layout.screen_card_menu

    override fun providePresentationModel(): CardMenuPm {
        return CardMenuPm()
    }

    override fun onBindPresentationModel(pm: CardMenuPm) {
        super.onBindPresentationModel(pm)

        viewManager = LinearLayoutManager(this.context)

        cardMenuAdapter = MenuCardAdapter(
            CARD_MENU_ITEM_VIEW_CONFIGS,
            R.layout.menu_card_layout
        )

        cardMenuItemsView = cardMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = cardMenuAdapter
        }
    }
}

class CardMenuPm : ScreenPresentationModel()