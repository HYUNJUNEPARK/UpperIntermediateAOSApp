package com.june.youtube.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.june.youtube.R
import com.june.youtube.activity.MainActivity
import com.june.youtube.databinding.FragmentPlayerBinding
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {
    private var _binding : FragmentPlayerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        motionSyncFragmentMotionLayoutAndMainMotionLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun motionSyncFragmentMotionLayoutAndMainMotionLayout() {
        binding.playerMotionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) { }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) { }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) { }
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                binding?.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
                    }
                }
            }
        })
    }
}