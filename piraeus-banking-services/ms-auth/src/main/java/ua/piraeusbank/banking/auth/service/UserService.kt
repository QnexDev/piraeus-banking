package ua.piraeusbank.banking.auth.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ua.piraeusbank.banking.auth.model.User
import ua.piraeusbank.banking.auth.respository.UserRepository

interface UserService {
    fun create(user: User)
}

@Service
class UserServiceImpl(@Autowired val userRepository: UserRepository) : UserService {

    override fun create(user: User) {
        val existing = userRepository.findById(user.username)
        existing.ifPresent { it -> throw IllegalArgumentException("user already exists: " + it.username) }

        val hash = encoder.encode(user.password)

        userRepository.save(user.copy(_password = hash))

        LOG.info("new user has been created: {}", user.username)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserServiceImpl::class.java)
        private val encoder = BCryptPasswordEncoder()
    }
}
