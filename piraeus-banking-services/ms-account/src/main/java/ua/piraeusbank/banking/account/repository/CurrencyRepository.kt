package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.domain.entity.CurrencyEntity

interface CurrencyRepository : JpaRepository<CurrencyEntity, Long> {

    fun getByCurrencyCode(code: String): CurrencyEntity

}