package com.brain.courses_app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentDetailBinding
import com.brain.courses_app.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        setView()
        binding.apply {
            fabTest.setOnClickListener {
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToTestFragment(
                        args.subject
                    )
                )
            }
            toolbarDetail.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    private fun setView() {
        binding.apply {
            detailViewModel.getLastDate(args.subject.id)
            detailViewModel.dateMutableLiveData.observe(viewLifecycleOwner) {
                txtLastDetail.text = getString(R.string.last_test).plus(it)
            }
            txtDesDetail.text = args.subject.description
            Glide.with(this@DetailFragment).load(args.subject.logo).into(imgDetail)
            collToolbar.title = args.subject.title
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}