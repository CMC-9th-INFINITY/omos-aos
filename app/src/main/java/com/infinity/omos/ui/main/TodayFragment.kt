package com.infinity.omos.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.infinity.omos.DetailRecordActivity
import com.infinity.omos.R
import com.infinity.omos.SelectCategoryActivity
import com.infinity.omos.adapters.CategoryListAdapter
import com.infinity.omos.adapters.TodayDjListAdapter
import com.infinity.omos.databinding.FragmentTodayBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SharedViewModel
import java.util.*

class TodayFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentTodayBinding

    private val userId = GlobalApplication.prefs.getInt("userId")

    private var musicId = ""
    private var musicTitle = ""
    private var artists = ""
    private var albumImageUrl = ""
    private var recordId = -1

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick()
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

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

        // 요일별 이미지 설정
        setMainImage()

        // 오늘의 노래
        viewModel.setTodayMusic()
        viewModel.getTodayMusic().observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.today = it
                this.artists = GlobalFunction.setArtist(it.artists)
                binding.tvTodayArtist.text = this.artists
                musicId = it.musicId
                musicTitle = it.musicTitle
                albumImageUrl = it.albumImageUrl
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

                if (it.isEmpty()){
                    binding.lnNodj.visibility = View.VISIBLE
                    binding.rvDj.visibility = View.GONE
                }
            }
        }
        viewModel.getStateRecommendDj().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.rvDj.visibility = View.GONE
                        binding.lnNodj.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.rvDj.visibility = View.VISIBLE
                        binding.lnNodj.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {
                        binding.lnNodj.visibility = View.VISIBLE
                        binding.rvDj.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }

        // 내가 사랑했던 노래
        viewModel.setMyLoveMusic(userId)
        viewModel.getMyLoveMusic().observe(viewLifecycleOwner) { music ->
            music?.let {
                binding.love = it
                binding.tvLoveArtist.text = GlobalFunction.setArtist(it.music.artists)
                recordId = it.recordId
            }
        }

        viewModel.getStateLoveMusic().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.fmLovedMusic.visibility = View.GONE
                        binding.lnNorecord.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.fmLovedMusic.visibility = View.VISIBLE
                        binding.lnNorecord.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {
                        binding.lnNorecord.visibility = View.VISIBLE
                        binding.fmLovedMusic.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }

        binding.btnTodayMusic.setOnClickListener {
            if (musicId != ""){
                val intent = Intent(context, SelectCategoryActivity::class.java)
                intent.putExtra("musicId", musicId)
                intent.putExtra("musicTitle", musicTitle)
                intent.putExtra("artists", artists)
                intent.putExtra("albumImageUrl", albumImageUrl)
                startActivity(intent)
            }
        }

        binding.fmLovedMusic.setOnClickListener {
            if (recordId != -1){
                val intent = Intent(context, DetailRecordActivity::class.java)
                intent.putExtra("postId", recordId)
                startActivity(intent)
            }
        }

        binding.btnWriteMyrecord.setOnClickListener {
            itemClickListener.onClick()
        }
    }

    private fun setMainImage(){
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)

        Glide.with(binding.imgMain.context)
            .load("https://omos-image.s3.ap-northeast-2.amazonaws.com/main/$day.png")
            .into(binding.imgMain)
    }
}