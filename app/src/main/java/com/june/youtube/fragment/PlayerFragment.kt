package com.june.youtube.fragment

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.june.youtube.R
import com.june.youtube.activity.MainActivity
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.databinding.FragmentPlayerBinding
import com.june.youtube.retrofit.MyRetrofit
import kotlin.math.abs

class PlayerFragment: BaseExoPlayerFragment<FragmentPlayerBinding>(R.layout.fragment_player) {
    private lateinit var videoAdapter: VideoAdapter

    override fun initView() {
        super.initView()

        motionSyncFragmentMotionLayoutAndMainMotionLayout()
        initRecyclerView()
        MyRetrofit(requireContext(), videoAdapter).videoList()
    }

    private fun initRecyclerView() {
        videoAdapter = VideoAdapter(itemClickedListener = { url, title ->
            play(url, title)
        })
        binding.fragmentRecyclerView.adapter = videoAdapter
        binding.fragmentRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun motionSyncFragmentMotionLayoutAndMainMotionLayout() {
        binding.playerMotionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) { }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) { }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) { }
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                binding.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
                    }
                }
            }
        })
    }
}