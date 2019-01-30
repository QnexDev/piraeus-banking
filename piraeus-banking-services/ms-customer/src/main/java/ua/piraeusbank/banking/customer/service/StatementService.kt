package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.common.config.AccountMoneyTransferMessage
import ua.piraeusbank.banking.customer.repository.StatementRepository

@Service
class StatementService(@Autowired val statementRepository: StatementRepository) {

    @Transactional(readOnly = true)
    fun getStatements(customerId: Long) = statementRepository.findAllByCustomerId(customerId)


    @JmsListener(destination = "account", containerFactory = "connectionFactory")
    fun receiveAccountMessage(message: AccountMoneyTransferMessage) {

    }
}