package ua.piraeusbank.banking.auth.service.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ua.piraeusbank.banking.auth.model.User
import ua.piraeusbank.banking.auth.respository.UserRepository

@Service
class UserDetailsServiceImpl(@Autowired var userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): User =
            userRepository.findById(username).orElseThrow { UsernameNotFoundException(username) }

}
