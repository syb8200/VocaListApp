package fastcampus.part1.fc_chapter7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fastcampus.part1.fc_chapter7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}