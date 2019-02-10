package ua.piraeusbank.banking.auth.respository

import org.springframework.data.jpa.repository.JpaRepository
import ua.piraeusbank.banking.auth.model.User

interface UserRepository : JpaRepository<User, String>
