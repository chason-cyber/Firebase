package tw.edu.pu.csim.tcyang.firebase.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tw.edu.pu.csim.tcyang.firebase.UserScoreModel
import tw.edu.pu.csim.tcyang.firebase.UserScoreRepository

class UserScoreViewModel : ViewModel() {
    private val userScoreRepository = UserScoreRepository()

    var message by mutableStateOf("訊息")
        private set

    fun addUser(userScore: UserScoreModel) {
        viewModelScope.launch {
            message = userScoreRepository.addUser(userScore)
        }
    }
    fun updateUser(userScore: UserScoreModel) {
        viewModelScope.launch {
            message = userScoreRepository.updateUser(userScore)
        }
    }
    fun deleteUser(userScore: UserScoreModel) {
        viewModelScope.launch {
            message = userScoreRepository.deleteUser(userScore)
        }
    }

}