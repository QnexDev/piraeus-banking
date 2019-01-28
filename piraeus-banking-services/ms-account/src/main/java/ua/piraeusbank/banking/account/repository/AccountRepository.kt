package ua.piraeusbank.banking.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.account.domain.Account

@Repository
interface AccountRepository : JpaRepository<Long, Account> {

}