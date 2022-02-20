package com.infinity.omos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.ActivityLoginBinding
import com.infinity.omos.databinding.ActivityRecordDetailBinding
import com.infinity.omos.databinding.ActivityRecordOnelineBinding
import com.infinity.omos.viewmodels.RecordDetailViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RecordDetailActivity : AppCompatActivity() {

    private val viewModel: RecordDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)

        var title = intent.getStringExtra("title")

        val binding = DataBindingUtil.setContentView<ActivityRecordDetailBinding>(this, R.layout.activity_record_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.record = MyRecord(title!!, "", "", "")

        initToolBar()
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_record, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_instar -> {
                Toast.makeText(this, "Instargram", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_more -> {
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}