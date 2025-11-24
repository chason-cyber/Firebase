package tw.edu.pu.csim.tcyang.firebase

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class UserScoreModel(
    var user: String = "",
    var score: Int = 0,

    @ServerTimestamp
    var timestamp: Date? = null
)

