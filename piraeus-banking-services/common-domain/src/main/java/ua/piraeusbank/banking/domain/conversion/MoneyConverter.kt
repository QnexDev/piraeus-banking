package ua.piraeusbank.banking.domain.conversion

import org.javamoney.moneta.FastMoney
import org.javamoney.moneta.Money
import ua.piraeusbank.banking.domain.entity.DEFAULT_CURRENCY
import java.math.BigDecimal
import java.util.*
import javax.money.MonetaryAmount
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class MoneyConverter : AttributeConverter<MonetaryAmount, BigDecimal> {

    override fun convertToDatabaseColumn(attribute: MonetaryAmount?): BigDecimal {
        return Optional.ofNullable(attribute).orElse(FastMoney
                .zero(DEFAULT_CURRENCY))
                .query(EXTRACT_BIG_DECIMAL)
    }

    override fun convertToEntityAttribute(dbData: BigDecimal): MonetaryAmount {
        return Money.of(dbData, DEFAULT_CURRENCY)
    }

    companion object {

        private val EXTRACT_BIG_DECIMAL = { m: MonetaryAmount ->
            m.number.numberValue(BigDecimal::class.java)
        }
    }
} 