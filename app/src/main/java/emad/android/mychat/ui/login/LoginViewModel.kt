package emad.android.mychat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emad.android.mychat.util.Constants
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
): ViewModel() {
    private val _loginEvent = MutableSharedFlow<LogInEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUserName(username: String) =
        username.length >= Constants.MIN_USERNAME_LENGTH

    fun connectUser(username: String){
        val trimmedUsername = username.trim()

        viewModelScope.launch {
            if(isValidUserName(trimmedUsername)){
                val result = client.connectGuestUser(
                    userId = trimmedUsername,
                    username = trimmedUsername
                ).await()
                if (result.isError){
                    _loginEvent.emit(LogInEvent.ErrorLogIn(result.error().message ?: "Unknown Error"))
                    return@launch
                }
                _loginEvent.emit(LogInEvent.Success)
            }else{
                _loginEvent.emit(LogInEvent.ErrorInputTooShort)
            }
        }
    }
    sealed class LogInEvent{
        object ErrorInputTooShort : LogInEvent()
        data class ErrorLogIn(val error: String) : LogInEvent()
        object Success : LogInEvent()
    }
}