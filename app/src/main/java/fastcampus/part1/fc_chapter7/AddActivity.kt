package fastcampus.part1.fc_chapter7

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.chip.Chip
import fastcampus.part1.fc_chapter7.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBinding
    private var originWord : Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
        binding.addButton.setOnClickListener {
            if (originWord == null) {
                add()
            } else {
                edit()
            }

        }
    }

    private fun initViews() {
        val types = listOf("명사", "동사", "대명사", "형용사", "부사", "감탄사", "전치사", "접속사")
        binding.typeChipGroup.apply {
            // 리스트인 types의 값들을 하나씩 꺼내서 addView로 칩 생성 (칩 == createChip(text))
            types.forEach { text ->
                addView(createChip(text))
            }
        }

        // getParcelableExtra가 deprecated가 되어서 코드를 추가해보았음
        originWord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("originWord", Word::class.java)
        } else {
            intent.getParcelableExtra("originWord")
        }

        originWord?.let { word ->
            // editText에 text를 세팅할 때에는 setText() 이용
            binding.textInputEditText.setText(word.text)
            binding.meanInputEditText.setText(word.mean)
            val selectedChip = binding.typeChipGroup.children.firstOrNull { (it as Chip).text == word.type } as? Chip
            selectedChip?.isChecked = true
        }
    }

    // Chip 생성
    private fun createChip(text: String) : Chip {
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }

    private fun add() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val word = Word(text, mean, type)

        // 데이터베이스 작업은 UI 스레드에서 진행X (작업시간이 오래 걸려서 ANR 발생 가능성 있음)
        Thread {
            // AppDatabase에서 getInstance로 접근 -> interface인 WordDao에 접근 -> insert
            AppDatabase.getInstance(this)?.wordDao()?.insert(word)
            runOnUiThread {
                Toast.makeText(this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent().putExtra("isUpdated", true)
            setResult(RESULT_OK, intent)
            finish()
        }.start()
    }

    private fun edit() {
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val editWord = originWord?.copy(text = text, mean = mean, type = type)

        Thread {
            editWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.update(word)
                val intent = Intent().putExtra("editWord", editWord)
                setResult(RESULT_OK, intent)
                runOnUiThread {
                    Toast.makeText(this, "수정을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }.start()
    }
}