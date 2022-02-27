package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.ALineCategoryListAdapter
import com.infinity.omos.databinding.ActivityCategoryBinding
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.CategoryViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_register.toolbar

class CategoryActivity : AppCompatActivity() {

    private val viewModel: CategoryViewModel by viewModels()

    private var ctg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCategoryBinding>(this, R.layout.activity_category)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        var category = intent.getStringExtra("category")
        initToolBar(category!!)

        // 카테고리 Record 가져오기
        when (category) {
            resources.getString(R.string.category1) -> {
                ctg = "A_LINE"
            }
            resources.getString(R.string.category2) -> {
                ctg = "OST"
            }
            resources.getString(R.string.category3) -> {
                ctg = "STORY"
            }
            resources.getString(R.string.category4) -> {
                ctg = "LYRICS"
            }
            else -> {
                ctg = "FREE"
            }
        }

        viewModel.setCategory(ctg, 0, 5, null,
            GlobalApplication.prefs.getLong("userId").toInt())

        viewModel.category.observe(this, Observer { category ->
            category?.let {
                if (ctg == "A_LINE"){
                    val mAdapter = ALineCategoryListAdapter(this, it)
                    recyclerView.apply{
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                } else {
                    val mAdapter = DetailCategoryListAdapter(this, it)
                    recyclerView.apply{
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        })
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