package com.brain.courses_app.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentProfileBinding
import com.brain.courses_app.view.LoginActivity
import com.brain.courses_app.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        setProfileView()
        binding.btnLogoutPro.setOnClickListener {
            showLogout()
        }
        binding.btnHistoryPro.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHistoryFragment())
        }
        return binding.root
    }

    private fun setProfileView() {
        profileViewModel.getUser()
        binding.apply {
            profileViewModel.userMutableLiveData.observe(viewLifecycleOwner) {
                progressBarProfile.visibility = View.GONE
                txtNamePro.text = it?.name
                txtEmailPro.text = it?.email
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLogout() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.apply {
            setTitle(getString(R.string.logout))
            setCancelable(true)
            setMessage(getString(R.string.logout_msg))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                profileViewModel.logout()
                startActivity(
                    Intent(
                        requireActivity(),
                        LoginActivity::class.java
                    )
                ).also { requireActivity().finish() }
            }
            setNegativeButton(getString(R.string.no)) { _, _ -> }
        }.create().show()
    }
}