package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.data.MyRecord
import com.infinity.omos.ui.AllRecordFragment
import com.infinity.omos.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        var category = intent.getStringExtra("category")
        initToolBar(category!!)

        // 카테고리 Record 가져오기
        var record: List<MyRecord>
        when (category) {
            resources.getString(R.string.category1) -> {
                record = AllRecordFragment.oneLineRecord
            }
            resources.getString(R.string.category2) -> {
                record = AllRecordFragment.myOstRecord
            }
            resources.getString(R.string.category3) -> {
                record = AllRecordFragment.myStoryRecord
            }
            resources.getString(R.string.category4) -> {
                record = AllRecordFragment.interpretRecord
            }
            else -> {
                record = AllRecordFragment.freeRecord
            }
        }

        val mAdapter = DetailCategoryListAdapter(this, record)
        recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initToolBar(category: String){
        toolbar.title = category
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}