package ua.piraeusbank.banking.client.ui.extensions

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.dmdev.rxpm.widget.InputControl


internal fun InputControl.validator(): ValidationBuilder {
    return ValidationBuilder(this)
}


interface ValidationRule {

    fun validate(inputText: String): Boolean

    fun getErrorMessage(): String

}


class NonEmptyRule(private val errorMessage: String) : ValidationRule {

    override fun validate(inputText: String) = !inputText.isEmpty()

    override fun getErrorMessage() = errorMessage
}

class MinLengthRule(private val errorMessage: String, private val minLength: Int) :
    ValidationRule {
    override fun validate(inputText: String) = inputText.length >= minLength

    override fun getErrorMessage() = errorMessage
}


class ValidationBuilder(val inputControl: InputControl) {

    private lateinit var errorConsumer: Consumer<String>
    private lateinit var observable: Observable<String>
    private val validationRules = ArrayList<ValidationRule>()

    fun nonEmpty(): ValidationBuilder {
        validationRules.add(NonEmptyRule("error 1"))
        return this
    }

    fun minLength(length: Int): ValidationBuilder {
        validationRules.add(MinLengthRule("error 2", length))
        return this
    }

    fun addErrorConsumer(consumer: Consumer<String>): ValidationBuilder {
        this.errorConsumer = consumer
        return this
    }

    fun addTextObservable(observable: Observable<String>): ValidationBuilder {
        this.observable = observable
        return this
    }

    fun build(): Disposable {
        errorConsumer.accept("")
        return observable
            .subscribe { text ->
                validationRules
                    .find { rule -> !rule.validate(text) }
                    .let { errorConsumer.accept(it?.getErrorMessage() ?: "") }
            }
    }

}
