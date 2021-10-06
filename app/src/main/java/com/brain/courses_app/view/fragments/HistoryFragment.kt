package com.brain.courses_app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentHistoryBinding
import com.brain.courses_app.model.TestResult
import com.brain.courses_app.view.adapters.HistoryAdapter
import com.brain.courses_app.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(), HistoryAdapter.OnItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var resultAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        resultAdapter = HistoryAdapter(this)
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        binding.rcvHistory.apply {
            setHasFixedSize(true)
            adapter = resultAdapter
        }
        historyViewModel.getResult()
        historyViewModel.results.observe(viewLifecycleOwner, {
            resultAdapter.submitList(it)
            binding.rcvHistory.visibility = View.VISIBLE
            binding.progressBarHistory.visibility = View.GONE
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(testResult: TestResult) {
        findNavController().navigate(
            HistoryFragmentDirections.actionHistoryFragmentToTestResultFragment(
                testResult.userAnswers.toTypedArray(), testResult.date, testResult.subject.title
            )
        )
    }
}