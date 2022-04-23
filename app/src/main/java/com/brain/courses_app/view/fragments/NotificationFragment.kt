package com.brain.courses_app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentNotificationBinding
import com.brain.courses_app.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        notificationViewModel.getIsChecked()
        notificationViewModel.isCheckedMutableLiveData.observe(viewLifecycleOwner) {
            binding.switchNotification.isChecked = it
        }
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            notificationViewModel.setShowNotification(isChecked)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}