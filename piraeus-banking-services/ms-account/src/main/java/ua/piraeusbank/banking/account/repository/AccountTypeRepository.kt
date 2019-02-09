package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.domain.entity.AccountTypeEntity
import java.util.*

interface AccountTypeRepository: JpaRepository<AccountTypeEntity, Long> {

    fun findByName(name: String): Optional<AccountTypeEntity>
}