package ua.piraeusbank.banking.card.service

import org.springframework.stereotype.Service
import ua.piraeusbank.banking.card.domain.BankCardNetworkCode
import ua.piraeusbank.banking.card.domain.BankCardNetworkCode.MASTERCARD
import ua.piraeusbank.banking.card.domain.BankCardNetworkCode.VISA
import ua.piraeusbank.banking.card.domain.BankCardNumber
import java.math.BigInteger
import kotlin.random.Random

interface SensitiveValueGenerator<T, V> {

    fun generate(params: T): V


}

typealias PinCodeGeneratorAlias = SensitiveValueGenerator<Empty, Short>

@Service("pinCodeGenerator")
class NaivePinCodeGenerator : PinCodeGeneratorAlias {

    override fun generate(params: Empty): Short = Random.nextInt(100, 999).toShort()

}

typealias SecurityCodeGeneratorAlias = SensitiveValueGenerator<Empty, Short>

@Service("securityCodeGenerator")
class NaiveSecurityCodeGenerator : SecurityCodeGeneratorAlias {

    override fun generate(params: Empty): Short = Random.nextInt(1000, 9999).toShort()
}

typealias CardNumberGeneratorAlias = SensitiveValueGenerator<BankCardNetworkCode, BankCardNumber>

@Service("cardNumberGenerator")
class NaiveCardNumberGenerator : CardNumberGeneratorAlias {

    override fun generate(params: BankCardNetworkCode): BankCardNumber = when (params) {
        VISA -> generateNumber(params)
        MASTERCARD -> generateNumber(params)
    }

    fun generateNumber(network: BankCardNetworkCode): BankCardNumber {
        val number =
                "${network.startDigit}${threeDigits()}${fourDigits()}${fourDigits()}${fourDigits()}".toBigInteger()

        val binCode = extractBin(number)

        return BankCardNumber(number, binCode)
    }

    fun extractBin(number: BigInteger): Int = number.toString().substring(0, 5).toInt()

    private fun threeDigits() = Random.nextInt(100, 999)
    private fun fourDigits() = Random.nextInt(1000, 9999)
}

object Empty