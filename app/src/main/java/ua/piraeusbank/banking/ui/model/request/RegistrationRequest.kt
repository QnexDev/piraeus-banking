package ua.piraeusbank.banking.ui.model.request

data class RegistrationRequest(val phone: String,
                               val email: String,
                               val password: String,
                               val name: String,
                               val lastName: String)