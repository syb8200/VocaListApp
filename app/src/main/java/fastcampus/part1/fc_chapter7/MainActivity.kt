package fastcampus.part1.fc_chapter7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import fastcampus.part1.fc_chapter7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var wordAdapter : WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dummyList = mutableListOf(
            Word("weather", "날씨", "명사"),
            Word("honey", "꿀", "명사"),
            Word("run", "실행하다", "동사")
        )

        wordAdapter = WordAdapter(dummyList)
        binding.wordRecyclerView.apply {
            // 어댑터 연결
            adapter = wordAdapter
            // apply 안에 있기 때문에 this가 아닌 applicationContext 사용
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }

    }
}