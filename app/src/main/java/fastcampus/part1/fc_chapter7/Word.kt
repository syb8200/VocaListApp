package fastcampus.part1.fc_chapter7

import androidx.room.Entity
import androidx.room.PrimaryKey

// data holding 역할 (상속 불가능, 반드시 하나 이상의 property 갖음)
@Entity(tableName = "word")
data class Word(
    val text : String,
    val mean : String,
    val type : String,

    // 자동으로 id 값이 자동으로 만들어진다.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
