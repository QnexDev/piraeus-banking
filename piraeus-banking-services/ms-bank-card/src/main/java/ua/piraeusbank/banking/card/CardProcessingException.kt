package ua.piraeusbank.banking.card

import java.lang.RuntimeException

class CardProcessingException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)