package ua.piraeusbank.banking.ui.screen.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_statement_action_layout.view.*
import ua.piraeusbank.banking.R
import ua.piraeusbank.banking.ui.model.StatementAction

class StatementAdapter(
    private val cardStatement: List<StatementAction>,
    private val layout: Int
) :
    RecyclerView.Adapter<StatementAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return cardStatement.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.actionDescr.text = cardStatement[position].description
        holder.transferType.setImageResource(
            when (cardStatement[position].transferType) {
                StatementAction.TransferType.INCOMING -> R.drawable.ic_arrow_double_left
                StatementAction.TransferType.OUTGOING -> R.drawable.ic_arrow_double_right
            }
        )
        holder.actionTimestamp.text = cardStatement[position].timestamp
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val actionDescr = view.actionDescr!!
        val transferType = view.transferType!!
        val actionTimestamp = view.actionTimestamp!!
    }
}