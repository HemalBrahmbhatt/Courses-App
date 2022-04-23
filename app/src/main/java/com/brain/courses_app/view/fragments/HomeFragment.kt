package com.brain.courses_app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentHomeBinding
import com.brain.courses_app.model.Subject
import com.brain.courses_app.view.adapters.SubjectAdapter
import com.brain.courses_app.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), SubjectAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var subjectAdapter: SubjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        subjectAdapter = SubjectAdapter(this)

        setList()

        binding.apply {
            btnList.setOnClickListener {
                rcvSubjects.layoutManager = LinearLayoutManager(activity)
            }
            btnGrid.setOnClickListener {
                val gridLayoutManager =
                    GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                rcvSubjects.layoutManager = gridLayoutManager
            }
        }
        return binding.root
    }

    private fun setList() {
        binding.toggleGrp.check(binding.btnGrid.id)
        binding.rcvSubjects.apply {
            setHasFixedSize(true)
            val gridLayoutManager =
                GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            binding.rcvSubjects.layoutManager = gridLayoutManager
            adapter = subjectAdapter
        }
        homeViewModel.getSubjectList()
        homeViewModel.subjects.observe(viewLifecycleOwner) {
            subjectAdapter.submitList(it)
        }
    }

    override fun onItemClick(subject: Subject) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                subject
            )
        )
        binding.toggleGrp.check(binding.btnGrid.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}