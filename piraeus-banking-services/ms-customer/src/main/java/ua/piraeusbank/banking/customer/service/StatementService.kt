package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.customer.repository.StatementRepository
import ua.piraeusbank.banking.domain.entity.StatementRecordEntity
import ua.piraeusbank.banking.domain.model.AccountMoneyTransferMessage
import java.time.LocalDate

@Service
class StatementService(@Autowired val statementRepository: StatementRepository) {

    @Transactional(readOnly = true)
    fun getStatements(customerId: Long) = statementRepository.findAllByCustomerId(customerId)


    @JmsListener(destination = "account", containerFactory = "connectionFactory")
    fun handleAccountMessage(message: AccountMoneyTransferMessage) {
        val transaction = message.transaction
        val transferAmount = transaction.amount

        val sourceAccount = transaction.sourceAccount
        val targetAccount = transaction.targetAccount

        statementRepository.saveAndFlush(StatementRecordEntity(
                date = LocalDate.now(),
                description = transaction.description,
                customer = sourceAccount?.customer!!,
                paidOut = transferAmount,
                type = transaction.type.toString()
        ))

        statementRepository.saveAndFlush(StatementRecordEntity(
                date = LocalDate.now(),
                description = transaction.description!!,
                customer = targetAccount?.customer!!,
                paidIn = transferAmount,
                type = transaction.type.toString()
        ))
    }
}