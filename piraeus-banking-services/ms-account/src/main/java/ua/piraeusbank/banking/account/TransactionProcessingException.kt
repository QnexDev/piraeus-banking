package ua.piraeusbank.banking.account

import java.lang.RuntimeException

class TransactionProcessingException(message: String, cause: Throwable? = null): RuntimeException()