package com.example.hoodalert.ui.components

class PasswordState :
    TextFieldState(validator = ::isPasswordValid, errorFor = ::passwordValidationError)

private fun isPasswordValid(password: String): Boolean {
    return password.isNotEmpty()
}

@Suppress("UNUSED_PARAMETER")
private fun passwordValidationError(password: String): String {
    return "Invalid password"
}