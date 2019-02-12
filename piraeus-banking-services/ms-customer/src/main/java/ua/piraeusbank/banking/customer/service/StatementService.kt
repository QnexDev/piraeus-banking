package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.common.domain.StatementRecord
import ua.piraeusbank.banking.customer.repository.StatementRepository
import ua.piraeusbank.banking.domain.entity.StatementRecordEntity
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode
import ua.piraeusbank.banking.domain.model.AccountMoneyTransferMessage
import java.time.LocalDate

@Service
class StatementService(@Autowired val statementRepository: StatementRepository) {

    @Transactional(readOnly = true)
    fun getStatements(customerId: Long) =
            statementRepository.findAllByCustomerId(customerId).map {
                StatementRecord(
                        customerId = it.customer.customerId!!,
                        description = it.description,
                        type = it.type,
                        paidIn = it.paidIn,
                        paidOut = it.paidOut,
                        date = it.date,
                        statementId = it.statementId
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
                customer = sourceAccount?.customer!!,
                paidOut = transferAmount,
                type = transaction.type.code.name
        ))

        statementRepository.saveAndFlush(StatementRecordEntity(
                date = LocalDate.now(),
                description = transaction.description,
                customer = targetAccount?.customer!!,
                paidIn = transferAmount,
                type =  transaction.type.code.name
        ))
    }
}