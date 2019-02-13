package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.common.domain.StatementRecord
import ua.piraeusbank.banking.customer.repository.StatementRepository
import ua.piraeusbank.banking.domain.entity.StatementRecordEntity
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode
import ua.piraeusbank.banking.domain.entity.TransferType
import ua.piraeusbank.banking.domain.model.AccountMoneyTransferMessage
import java.time.LocalDate

@Service
class StatementService(@Autowired val statementRepository: StatementRepository) {

    @Transactional(readOnly = true)
    fun getStatements(customerId: Long) =
            statementRepository.findAllByCustomerId(customerId).map {
                StatementRecord(
                        customerName = it.customerName,
                        customerLastname = it.customerLastname,
                        description = it.description,
                        type = it.type.name,
                        paidIn = it.paidIn,
                        paidOut = it.paidOut,
                        date = it.date,
                        statementId = it.statementId,
                        timestamp = it.timestamp
                )
            }


    @JmsListener(destination = "account", containerFactory = "connectionFactory")
    fun handleAccountMessage(message: AccountMoneyTransferMessage) {
        if (message.transaction.type.code != TransactionTypeCode.TRANSFER) {
            return
        }
        val transaction = message.transaction
        val transferAmount = transaction.amount

        val sourceAccount = transaction.sourceAccount
        val targetAccount = transaction.targetAccount

        statementRepository.saveAndFlush(StatementRecordEntity(
                date = LocalDate.now(),
                description = transaction.description,
                customerLastname = sourceAccount?.customer?.name!!,
                customerName = sourceAccount.customer.lastName,
                paidOut = transferAmount,
                type = TransferType.OUTGOING,
                timestamp = transaction.timestamp,
                customerId = sourceAccount.customer.customerId!!

        ))

        statementRepository.saveAndFlush(StatementRecordEntity(
                date = LocalDate.now(),
                description = transaction.description,
                customerLastname = targetAccount?.customer?.name!!,
                customerName = targetAccount.customer.lastName,
                paidIn = transferAmount,
                type = TransferType.INCOMING,
                timestamp = transaction.timestamp,
                customerId = targetAccount.customer.customerId!!
        ))
    }
}