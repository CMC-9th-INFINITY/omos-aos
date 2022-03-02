package com.infinity.omos

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.ALineCategoryListAdapter
import com.infinity.omos.databinding.ActivityCategoryBinding
import com.infinity.omos.repository.Repository
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.CategoryViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_main.*
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
            resources.getString(R.string.a_line) -> {
                ctg = "A_LINE"
            }
            resources.getString(R.string.ost) -> {
                ctg = "OST"
            }
            resources.getString(R.string.story) -> {
                ctg = "STORY"
            }
            resources.getString(R.string.lyrics) -> {
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

        viewModel.stateCategory.observe(this, Observer { state ->
            state?.let {
                when(it){
                    Repository.ApiState.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Repository.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    Repository.ApiState.TOKEN -> {
                        binding.progressBar.visibility = View.GONE
                        viewModel.setCategory(ctg, 0, 5, null,
                            GlobalApplication.prefs.getLong("userId").toInt())
                    }

                    else -> {
                        binding.progressBar.visibility = View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_sort -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}