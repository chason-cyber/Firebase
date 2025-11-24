package tw.edu.pu.csim.tcyang.firebase.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.edu.pu.csim.tcyang.firebase.UserScoreModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun UserScoreScreen( userScoreViewModel: UserScoreViewModel = viewModel()
) {
    var user by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var user = userScoreViewModel.user
        TextField(
            value = user,
            // 當值改變時，呼叫 ViewModel 的 onUserChange 函式
            onValueChange = { userScoreViewModel.onUserChange(it) },
            label = { Text("姓名") },
            placeholder = { Text("請輸入您的姓名") }
        )
        Text("您輸入的姓名是：$user")
        Spacer(modifier = Modifier.size(10.dp))


        TextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("分數") },
            placeholder = { Text("請輸入您的分數") },
            keyboardOptions = KeyboardOptions
                (keyboardType = KeyboardType.Number)
        )
        Text("您的分數是：$score")
        Spacer(modifier = Modifier.size(10.dp))



        Button(onClick = {
            var userScore = UserScoreModel("宇謙", 39)
            userScoreViewModel.addUser(userScore)
        }) {
            Text("新增資料")
        }
        Button(onClick = {
            // 在按鈕點擊時，直接呼叫 ViewModel 的函式
            var userScore = UserScoreModel( user,score.toInt())
            userScoreViewModel.updateUser(userScore)
        }) {
            Text("新增/異動資料")
        }

        Button(onClick = {
            // 在按鈕點擊時，直接呼叫 ViewModel 的函式
            var userScore = UserScoreModel("宇謙", 21)
            userScoreViewModel.deleteUser(userScore)
        }) {
            Text("刪除資料")
        }



        Button(onClick = {
            // 在按鈕點擊時，直接呼叫 ViewModel 的函式
            var userScore = UserScoreModel("宇謙", 21)
            userScoreViewModel.getUser(userScore)
        }) {
            Text("查詢資料")
        }



        Button(onClick = {
            // 在按鈕點擊時，直接呼叫 ViewModel 的函式
            var userScore = UserScoreModel("宇謙", 21)
            userScoreViewModel.orderUser(userScore)
        }) {
            Text("查詢前三名")
        }
        Text(userScoreViewModel.message)
    }


}