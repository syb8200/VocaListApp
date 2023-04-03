package fastcampus.part1.fc_chapter7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fastcampus.part1.fc_chapter7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var wordAdapter : WordAdapter

    // AddActivity에서 데이터가 추가됐기 때만 화면에 데이터 업데이트
    private val updateAddWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false
        if(result.resultCode == RESULT_OK && isUpdated) {
            updateAddWordResult()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initRecyclerView()

        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let {
                updateAddWordResult.launch(it)
            }
        }
    }

    private fun initRecyclerView() {
        wordAdapter = WordAdapter(mutableListOf(), this)
        binding.wordRecyclerView.apply {
            // 어댑터 연결
            adapter = wordAdapter
            // apply 안에 있기 때문에 this가 아닌 applicationContext 사용
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            // 디바이더 (구분선)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }

        Thread {
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            wordAdapter.list.addAll(list)
            runOnUiThread {
                // 데이터가 바뀐 것을 반영을 해줘야 함 (UI가 변함)
                // notifyDataSetChanged() : 부하가 많이 걸리게 함 (리스트가 길어질수록 다시 화면을 로드하는 속도가 느려짐)
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun updateAddWordResult() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let { word ->
                wordAdapter.list.add(0, word)
                runOnUiThread{
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text}가 클릭되었습니다.", Toast.LENGTH_SHORT).show()
    }
}