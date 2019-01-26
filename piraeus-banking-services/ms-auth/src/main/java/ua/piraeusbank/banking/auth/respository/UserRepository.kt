package ua.piraeusbank.banking.auth.respository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.auth.model.User

@Repository
interface UserRepository : JpaRepository<User, String>
