package com.infinity.omos.ui.bottomnav

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.AlbumActivity
import com.infinity.omos.R
import com.infinity.omos.SelectCategoryActivity
import com.infinity.omos.adapters.CategoryListAdapter
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.MyDjListAdapter
import com.infinity.omos.adapters.TodayDjListAdapter
import com.infinity.omos.databinding.FragmentMyRecordBinding
import com.infinity.omos.databinding.FragmentTodayBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.utils.MyReceiver
import com.infinity.omos.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_today.*

class TodayFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentTodayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 오늘의 노래
        viewModel.setTodayMusic()
        viewModel.getTodayMusic().observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.today = it
                binding.tvTodayArtist.text = GlobalFunction.setArtist(it.artists)
            }
        }

        // 인기있는 레코드
        val fAdapter = CategoryListAdapter(requireContext())
        binding.rvFamous.apply {
            adapter = fAdapter
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        }
        viewModel.setFamousRecord()
        viewModel.getFamousRecord().observe(viewLifecycleOwner) { record ->
            record?.let {
                fAdapter.setRecords(it)
            }
        }

        // 추천 DJ
        val dAdapter = TodayDjListAdapter(requireContext())
        binding.rvDj.apply {
            adapter = dAdapter
            layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        }
        viewModel.setRecommendDj()
        viewModel.getRecommendDj().observe(viewLifecycleOwner) { dj ->
            dj?.let {
                dAdapter.setDj(it)
            }
        }

//        // 내가 사랑했던 노래
//        viewModel.setMyLoveMusic()
//        viewModel.getMyLoveMusic().observe(viewLifecycleOwner) { music ->
//            music?.let {
//                binding.love = it
//                binding.tvLoveArtist.text = GlobalFunction.setArtist(it.artists)
//            }
//        }
    }
}