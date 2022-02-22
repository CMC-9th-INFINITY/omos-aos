package com.infinity.omos.ui.bottomnav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.R
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.MyDjListAdapter
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.FragmentMyDjBinding
import com.infinity.omos.viewmodels.SharedViewModel

class MyDjFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentMyDjBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Toast.makeText(context, "페이지 안뜨면 전체레코드 페이지 갔다와보세요. (해당 페이지 API 연동 미완료)",Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dj, container, false)
        activity?.let{
            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        val mAdapter = MyDjListAdapter(requireContext())
        binding.rvMydj.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        }

        val rAdapter = DetailCategoryListAdapter(requireContext(), AllRecordFragment.oneLineRecord)
        binding.rvRecord.apply {
            adapter = rAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        mAdapter.setItemClickListener(object : MyDjListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, records: List<MyRecord>?) {
                // TODO: 수정 필요
                if (position == 0){
                    rAdapter.updateCategory(AllRecordFragment.oneLineRecord)
                }else if (position == 1){
                    rAdapter.updateCategory(AllRecordFragment.myOstRecord)
                } else if (position == 2){
                    rAdapter.updateCategory(AllRecordFragment.myStoryRecord)
                } else{
                    rAdapter.updateCategory(AllRecordFragment.freeRecord)
                }
            }
        })

        return binding.root
    }
}