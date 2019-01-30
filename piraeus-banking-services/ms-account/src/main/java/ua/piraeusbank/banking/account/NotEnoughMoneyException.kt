package ua.piraeusbank.banking.account

import java.lang.RuntimeException

open class TransactionProcessingException(message: String, cause: Throwable? = null): RuntimeException()

class NotEnoughMoneyException(message: String, cause: Throwable? = null): TransactionProcessingException(message, cause)