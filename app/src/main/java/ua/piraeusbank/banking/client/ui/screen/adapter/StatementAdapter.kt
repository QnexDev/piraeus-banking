package ua.piraeusbank.banking.client.ui.screen.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.statement_record_layout.view.*
import ua.piraeusbank.banking.client.R
import ua.piraeusbank.banking.client.ui.model.StatementRecord
import ua.piraeusbank.banking.client.ui.model.TransferType

class StatementAdapter(
    private val cardStatement: List<StatementRecord>,
    private val layout: Int,
    private val extendedView: Boolean = false
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
        val context = holder.rootView.context

        val statementRecord = cardStatement[position]
        holder.actionDescr.text = statementRecord.description
        val transferType = when (statementRecord.transferType) {
            TransferType.INCOMING -> R.drawable.ic_arrow_double_left
            TransferType.OUTGOING -> R.drawable.ic_arrow_double_right
        }
        holder.transferType.setImageResource(
            transferType
        )
        holder.actionTimestamp.text = statementRecord.timestamp

        holder.recordAmount.text = statementRecord.amount

        when (statementRecord.transferType) {
            TransferType.INCOMING -> {
                holder.recordAmount.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            TransferType.OUTGOING -> {
                holder.recordAmount.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }

        if (extendedView) {
            when (statementRecord.transferType) {
                TransferType.INCOMING -> {
                    holder.cardLabel.text = statementRecord.senderCard
                    holder.actionLabel.text = context.getString(R.string.statement_sender_label_txt)
                    holder.accountLabel.text = statementRecord.senderAccount
                }
                TransferType.OUTGOING -> {
                    holder.cardLabel.text = statementRecord.recipientCard
                    holder.actionLabel.text = context.getString(R.string.statement_recipient_label_txt)
                    holder.accountLabel.text = statementRecord.recipientAccount
                }
            }
        } else {
            holder.extraSectionView.visibility = View.GONE
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootView = view
        val extraSectionView = view.extraSectionView!!
        val actionDescr = view.actionDescr!!
        val transferType = view.transferType!!
        val recordAmount = view.recordAmount!!
        val actionTimestamp = view.actionTimestamp!!
        val cardLabel = view.cardLabel!!
        val actionLabel = view.actionLabel!!
        val accountLabel = view.accountLabel!!
    }
}