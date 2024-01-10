package com.example.hoodalert.ui.viewmodel.auth


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hoodalert.data.AppDataContainer
import com.example.hoodalert.data.model.User
import java.util.Date

class RegisterViewModel(private val appContainer: AppDataContainer) : ViewModel() {
    var userUiState by mutableStateOf(UserUiState())
        private set

    fun updateUiState(userDetails: UserDetails) {
        userUiState =
            UserUiState(
                userDetails = userDetails,
                isEntryValid = validateInput(userDetails)
            )
    }

    suspend fun saveUser() {
        if (validateInput()) {
            appContainer.usersRepository.insertUser(userUiState.userDetails.toUser())
        }
    }

    private fun validateInput(uiState: UserDetails = userUiState.userDetails): Boolean {
        return with(uiState) {
            email.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank() && password.isNotBlank()
        }
    }
}

data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
)

data class UserDetails(
    val id: Int = 0,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = ""
)

fun UserDetails.toUser(): User = User(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    password = password,
    createdAt = Date(),
    updatedAt = Date()
)

fun User.toUserUiState(isEntryValid: Boolean = false): UserUiState = UserUiState(
    userDetails = this.toUserDetails(),
    isEntryValid = isEntryValid
)

fun User.toUserDetails(): UserDetails = UserDetails(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    password = password
)
