package com.infinity.omos.ui.bottomnav

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.R
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.MyDjListAdapter
import com.infinity.omos.databinding.FragmentMyDjBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.CustomDialog
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SharedViewModel

class MyDjFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentMyDjBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    private val fromUserId = GlobalApplication.prefs.getInt("userId")

    private var page = 0
    private val pageSize = 5
    private var isLoading = false
    private var isFirst = true

    private var postId = -1
    private var tmpUserId = -1
    private lateinit var mAdapter: MyDjListAdapter
    private lateinit var rAdapter: DetailCategoryListAdapter
    private lateinit var dlg: CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
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

        mAdapter = MyDjListAdapter(requireContext())
        binding.rvMydj.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        }

        dlg = CustomDialog(requireContext())

        rAdapter = DetailCategoryListAdapter(requireContext())
        binding.rvRecord.apply {
            adapter = rAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        rAdapter.setItemClickListener(object: DetailCategoryListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, postId: Int) {
                val dlg = CustomDialog(context!!)
                dlg.show("??? ???????????? ??????????????????????", "??????")

                dlg.setOnOkClickedListener { content ->
                    when(content){
                        "yes" -> {
                            viewModel.reportObject(fromUserId, postId, null, null, "Record")
                            showProgress()
                        }
                    }
                }
            }
        })

        viewModel.getStateReportRecord().observe(viewLifecycleOwner) { state ->
            state?.let {
                dismissProgress()
                if (it.state) {
                    val intent = Intent("RECORD_UPDATE")
                    intent.putExtra("isDelete", true)
                    intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                    requireContext().sendBroadcast(intent)

                    if (tmpUserId == -1){
                        setFirst()
                    } else {
                        isFirst = false
                        viewModel.setMyDjRecord(fromUserId, tmpUserId)
                    }
                    binding.rvRecord.scrollToPosition(0)

                    Toast.makeText(context, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "??????", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.setMyDj(fromUserId)
        viewModel.getMyDj().observe(viewLifecycleOwner) { dj ->
            dj?.let {
                mAdapter.setDj(it)

                if (it.isEmpty()){
                    binding.swipeRefreshLayout.visibility = View.GONE
                    binding.lnNodj.visibility = View.VISIBLE
                } else {
                    binding.lnNodj.visibility = View.GONE
                }
            }
        }

        viewModel.getStateMyDj().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.swipeRefreshLayout.visibility = View.GONE
                        binding.lnNodj.visibility = View.GONE
                    }

                    Constant.ApiState.DONE -> {
                        binding.swipeRefreshLayout.visibility = View.VISIBLE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                    else -> {}
                }
            }
        }

        viewModel.setDjAllRecords(fromUserId, null, pageSize)
        viewModel.getDjAllRecords().observe(viewLifecycleOwner) { record ->
            record?.let {
                isLoading = if (it.isEmpty()) {
                    rAdapter.notifyItemRemoved(rAdapter.itemCount)
                    true
                } else {
                    rAdapter.addCategory(it)
                    rAdapter.notifyItemRangeInserted(page * pageSize, it.size)
                    postId = it[it.lastIndex].recordId
                    false
                }
            }
        }

        viewModel.getStateDjAllRecords().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        if (page == 0){
                            // ?????? API ?????? ??????
                            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) // ?????? ?????? ??????
                            binding.rvRecord.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                            binding.lnNorecord.visibility = View.GONE
                        }
                    }

                    Constant.ApiState.DONE -> {
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.rvRecord.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                    else -> {}
                }
            }
        }

        viewModel.getMyDjRecord().observe(viewLifecycleOwner) { record ->
            record?.let {
                rAdapter.setCategory(it)
            }
        }

        viewModel.getStateDjRecord().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        // ?????? API ?????? ??????
                        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.rvRecord.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.lnNorecord.visibility = View.GONE
                    }

                    Constant.ApiState.DONE -> {
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.rvRecord.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                    else -> {}
                }
            }
        }

        mAdapter.setItemClickListener(object : MyDjListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, toUserId: Int) {
                // ??? ??? ??? ?????? ??? ??? ?????? ??????
                if (position == -1){
                    setFirst()
                } else {
                    isFirst = false
                    tmpUserId = toUserId
                    viewModel.setMyDjRecord(fromUserId, toUserId)
                }
                binding.rvRecord.scrollToPosition(0)
            }
        })

        // ?????? ?????????
        binding.rvRecord.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // ???????????? ?????? ??????????????? ??????
                if (!binding.rvRecord.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading && isFirst && itemTotalCount > -1) {
                    isLoading = true
                    page ++
                    rAdapter.deleteLoading()
                    viewModel.setDjAllRecords(fromUserId, postId, pageSize)
                }
            }
        })

        // ???????????? ??????
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (tmpUserId == -1){
                setFirst()
            } else {
                isFirst = false
                viewModel.setMyDjRecord(fromUserId, tmpUserId)
            }
            binding.rvRecord.scrollToPosition(0)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }

    private fun showProgress(){
        val handler: Handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                dlg.showProgress()
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    private fun dismissProgress(){
        val handler: Handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                dlg.dismissProgress()
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    private fun setFirst(){
        page = 0
        isLoading = false
        isFirst = true
        tmpUserId = -1
        rAdapter.clearCategory()
        viewModel.setDjAllRecords(fromUserId, null, pageSize)
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setFirst()
                viewModel.setMyDj(fromUserId)
                binding.rvRecord.scrollToPosition(0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("DJ_UPDATE")
        )
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden){
            // ?????????, ????????? ??????
            var stateLike = false
            val saveHeartList = rAdapter.getSaveHeart()
            for (i in saveHeartList){
                viewModel.saveLike(i, fromUserId)
                stateLike = true
            }

            val deleteHeartList = rAdapter.getDeleteHeart()
            for (i in deleteHeartList){
                viewModel.deleteLike(i, fromUserId)
                stateLike = true
            }

            var stateScrap = false
            val saveScrapList = rAdapter.getSaveScrap()
            for (i in saveScrapList){
                viewModel.saveScrap(i, fromUserId)
                stateScrap = true
            }

            val deleteScrapList = rAdapter.getDeleteScrap()
            for (i in deleteScrapList){
                viewModel.deleteScrap(i, fromUserId)
                stateScrap = true
            }

            if (stateLike){
                val intent = Intent("LIKE_UPDATE")
                intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                context?.sendBroadcast(intent)
            }
            if (stateScrap){
                val intent = Intent("SCRAP_UPDATE")
                intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
                context?.sendBroadcast(intent)
            }

            if (stateLike || stateScrap){
                setFirst()
                viewModel.setMyDj(fromUserId)
                binding.rvRecord.scrollToPosition(0)
            }
        }
    }
}