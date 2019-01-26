package ua.piraeusbank.banking.auth.model

import org.springframework.data.annotation.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "User")
data class User(@Id val _username: String, val _password: String) : UserDetails {


    override fun getPassword(): String{
        return this._password
    }

    override fun getUsername(): String {
        return this._username
    }

    override fun getAuthorities(): List<GrantedAuthority>? {
        return null
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}