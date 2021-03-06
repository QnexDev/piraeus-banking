package ua.piraeusbank.banking.auth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ua.piraeusbank.banking.auth.model.User
import ua.piraeusbank.banking.auth.service.UserService
import ua.piraeusbank.banking.domain.model.AuthUser
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @RequestMapping(value = ["/current"], method = [RequestMethod.GET])
    fun getUser(principal: Principal): Principal {
        return principal
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = [RequestMethod.POST])
    fun createUser(@Valid @RequestBody user: AuthUser) {
        userService.create(User(user.username, user.password))
    }
}
