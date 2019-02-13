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
    val statementRecords: MutableList<StatementRecord>,
    private val layout: Int,
    private val extendedView: Boolean = false
) :
    RecyclerView.Adapter<StatementAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return statementRecords.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.rootView.context

        val statementRecord = statementRecords[position]
        holder.actionDescr.text = statementRecord.description
        val type = TransferType.valueOf(statementRecord.type)
        val transferType = when (type) {
            TransferType.INCOMING -> R.drawable.ic_arrow_double_left
            TransferType.OUTGOING -> R.drawable.ic_arrow_double_right
        }
        holder.transferType.setImageResource(
            transferType
        )
        holder.actionTimestamp.text = statementRecord.timestamp.toString()


        when (type) {
            TransferType.INCOMING -> {
                holder.recordAmount.text = statementRecord.paidIn?.amount.toString()
                holder.recordAmount.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            TransferType.OUTGOING -> {
                holder.recordAmount.text = statementRecord.paidOut?.amount.toString()

                holder.recordAmount.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }

        if (extendedView) {
            when (type) {
                TransferType.INCOMING -> {
                    holder.cardLabel.text = "*4235"
                    holder.actionLabel.text = context.getString(R.string.statement_sender_label_txt)
                    holder.accountLabel.text = statementRecord.customerName + " " + statementRecord.customerLastname
                }
                TransferType.OUTGOING -> {
                    holder.cardLabel.text = "*5235"
                    holder.actionLabel.text = context.getString(R.string.statement_recipient_label_txt)
                    holder.accountLabel.text = statementRecord.customerName + " " + statementRecord.customerLastname
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