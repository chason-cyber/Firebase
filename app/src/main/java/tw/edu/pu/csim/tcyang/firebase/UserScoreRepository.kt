package tw.edu.pu.csim.tcyang.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.Timestamp // 確保引入 Timestamp

class UserScoreRepository {
    // 請注意：您的 Collection 名稱在所有 CRUD 函式中都是 "UserScore" (單數)
    // 建議保持一致性，如果您的 ViewModel/UI 使用的是 "UserScores" (複數)，請同步修改。
    val db = Firebase.firestore
    private val collectionName = "UserScore" // 統一定義 Collection 名稱

    // 日期時間格式化器，用於將 Timestamp 轉換為字串
    private val dateTimeFormatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    suspend fun addUser(userScore: UserScoreModel): String {
        return try {
            // 新增時將 createTime 設定為當前時間 (Timestamp.now())
            val dataWithTime = userScore.copy(createTime = Timestamp.now())

            // 使用 .add() 讓 Firestore 自動產生 Document ID
            val documentReference =
                db.collection(collectionName)
                    .add(dataWithTime)
                    .await()
            " 新增資料成功！Document ID:\n ${documentReference.id}"
        } catch (e: Exception) {
            "新增資料失敗：${e.message}"
        }
    }

    // 建議將 updateUser 修改為 set(userScore) 或 update(data)
    // 您目前是使用 Document ID 為 userScore.user，請確保您的 document ID 是姓名。
    suspend fun updateUser(userScore: UserScoreModel): String {
        return try {
            // 異動時也更新時間戳記
            val data = hashMapOf(
                "user" to userScore.user,
                "score" to userScore.score,
                "createTime" to Timestamp.now()
            )

            db.collection(collectionName)
                .document(userScore.user) // 假設 Document ID 就是 userScore.user
                .set(data as Map<String, Any>) // 使用 set() 異動或新增
                .await()
            "新增/異動資料成功！Document ID:\n ${userScore.user}"
        } catch (e: Exception) {
            "新增/異動資料失敗：${e.message}"
        }
    }

    suspend fun deleteUser(userScore: UserScoreModel): String {
        return try {
            db.collection(collectionName)
                .document(userScore.user)
                .delete()
                .await()
            "🗑刪除資料成功！Document ID:\n ${userScore.user}"
        } catch (e: Exception) {
            "刪除資料失敗：${e.message}"
        }
    }

    // 您的 getUser 函式目前是硬編碼查詢 "子青"，建議改成根據傳入的 userScore.user 查詢
    suspend fun getUser(userScore: UserScoreModel): String {
        return try {
            val querySnapshot = db.collection(collectionName)
                .whereEqualTo("user", userScore.user) // 根據傳入的姓名查詢
                .get().await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val result = document.toObject<UserScoreModel>()
                "查詢成功！${result?.user} 的分數是 ${result?.score}"
            } else {
                " 查詢失敗：找不到使用者 ${userScore.user} 的資料。"
            }
        } catch (e: Exception) {
            "查詢資料失敗：${e.message}"
        }
    }

    /**
     * 【修正後的函式】
     * 根據分數遞減排序，列出前三名，並標註名次、姓名、分數及系統存入日期時間。
     */
    suspend fun orderUser(): String {
        return try {
            val querySnapshot = db.collection(collectionName)
                // 1. 依分數遞減排序 (分數高者在前)
                .orderBy("score", Query.Direction.DESCENDING)
                // 2. 依時間升序排序 (分數相同時，先存入者在前)
                .orderBy("createTime", Query.Direction.ASCENDING)
                // 3. 限制前 3 筆
                .limit(3)
                .get().await()

            if (querySnapshot.isEmpty) {
                return "📋 抱歉，資料庫目前無相關資料"
            }

            // 建立包含名次、分數、時間的格式化字串
            val results = querySnapshot.documents.mapIndexed { index, document ->
                val userScore = document.toObject<UserScoreModel>()
                val rank = index + 1 // 名次從 1 開始

                userScore?.let {
                    // 格式化時間
                    val timeString = it.createTime?.toDate()?.let { date ->
                        dateTimeFormatter.format(date)
                    } ?: "N/A"

                    // 組合標註訊息
                    "名次: $rank. 姓名: ${it.user}, 分數: ${it.score}, 存入時間: $timeString"
                } ?: ""
            }.filter { it.isNotEmpty() } // 濾掉轉換失敗的空字串

            // 組合最終訊息
            return "🏆 查詢成功！分數排行榜 (前 ${results.size} 名)：\n" + results.joinToString(separator = "\n")

        } catch (e: Exception) {
            " 查詢資料失敗：${e.message}"
        }
    }
}