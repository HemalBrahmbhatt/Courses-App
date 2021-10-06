package com.brain.courses_app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentTestResultBinding
import com.brain.courses_app.view.adapters.ResultAdapter

class TestResultFragment : Fragment() {
    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!
    private val args: TestResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_test_result, container, false)
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        binding.apply {
            val userAnswers = args.userAnswer
            var score = 0
            for (i in userAnswers) {
                if (i.answer == i.correct) {
                    score++
                }
            }
            val p = (score * 100) / 5
            circularIndicator.progress = p
            txtProgress.text = p.toString().plus("%")
            txtResult.text = score.toString().plus(getString(R.string.score_out_of_5))
            txtSubjectResult.text = args.subName
            txtDateResult.text = args.dt
            rcvResult.setHasFixedSize(true)
            val list = args.userAnswer.toList()
            val adapter = ResultAdapter(requireActivity(), list)
            rcvResult.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}