package com.june.ott

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.june.ott.ExtensionFunction.Companion.dpToPx

class MainActivity : IntroUiActivity() {
    private var isGatheringMotionAnimating: Boolean = false //애니메이션 효과 실행 시 true 끝나면 false

    override fun initViews() {
        super.initViews()

        initScrollViewListeners()
    }

    private fun initScrollViewListeners() {
        binding.gatheringDigitalThingsMotionLayout.setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                isGatheringMotionAnimating = true
            }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                isGatheringMotionAnimating = false
            }
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) { }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) { }
        })

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrolledValue = binding.scrollView.scrollY
            if (scrolledValue > 150f.dpToPx(this@MainActivity).toInt()) {
                //스크롤 뷰가 특정 높이로 올라왔을 때
                if (isGatheringMotionAnimating.not()) { //애니메이션 이펙트 off 상태 -> 트랜지션 종료
                    binding.gatheringDigitalThingsMotionLayout.transitionToEnd()
                    binding.buttonShownMotionLayout.transitionToEnd()
                }
            } else {
                if (isGatheringMotionAnimating.not()) { //애니메이션 이펙트 off 상태 -> 트랜지션 시작 -> 이펙트 on
                    binding.gatheringDigitalThingsMotionLayout.transitionToStart()
                    binding.buttonShownMotionLayout.transitionToStart()
                }
            }
        }
    }
}