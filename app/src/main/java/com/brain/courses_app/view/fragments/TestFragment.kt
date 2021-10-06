package com.brain.courses_app.view.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brain.courses_app.R
import com.brain.courses_app.databinding.FragmentTestBinding
import com.brain.courses_app.model.Questions
import com.brain.courses_app.model.UserAnswer
import com.brain.courses_app.view.MainActivity
import com.brain.courses_app.view.adapters.QuestionAdapter
import com.brain.courses_app.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TestFragment : Fragment(), QuestionAdapter.OnButtonClickListener {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!
    private val args: TestFragmentArgs by navArgs()
    private val testViewModel: TestViewModel by viewModels()
    private lateinit var adapter: QuestionAdapter
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        createNotificationChannel()
        getQuestions(true)
        binding.apply {
            txtTestTitle.text = args.subject.title
            btnRetry.setOnClickListener {
                tryAgain()
            }
        }
        return binding.root
    }

    private fun getQuestions(isFirstTime: Boolean) {
        testViewModel.getQuestions(args.subject.id)
        testViewModel.questionsMutableLiveData.observe(viewLifecycleOwner, {
            if (it == emptyList<Questions>()) {
                if (isFirstTime) {
                    noInternetVisibility()
                }
            } else {
                setAdapter(it)
            }
        })
    }

    private fun tryAgain() {
        binding.apply {
            txtNoInternet.visibility = View.GONE
            btnRetry.visibility = View.GONE
            progressBarTest.visibility = View.VISIBLE
        }
        getQuestions(false)
    }

    private fun setAdapter(list: List<Questions>) {
        adapter = QuestionAdapter(requireActivity(), list, this)
        binding.apply {
            viewPager.visibility = View.VISIBLE
            viewPager.isUserInputEnabled = false
            viewPager.adapter = adapter
            progressBarTest.visibility = View.GONE
            txtNoInternet.visibility = View.GONE
            btnRetry.visibility = View.GONE
        }
    }

    private fun noInternetVisibility() {
        binding.apply {
            progressBarTest.visibility = View.GONE
            viewPager.visibility = View.GONE
            txtNoInternet.visibility = View.VISIBLE
            btnRetry.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPreviousClick() {
        binding.viewPager.currentItem -= 1
    }

    override fun onNextClick(userAnswerSet: MutableSet<UserAnswer>, position: Int) {
        binding.apply {
            if (position == 4) {
                progressBarTest.visibility = View.VISIBLE
                val date =
                    SimpleDateFormat("dd/MM/yyyy - hh:mm:ss a", Locale.getDefault()).format(Date())
                testViewModel.getIsChecked()
                testViewModel.isNotificationCheckedMutableLiveData.observe(viewLifecycleOwner, {
                    if (it) {
                        setNotification(date, userAnswerSet)
                    }
                })
                storeResult(date, userAnswerSet)
            } else {
                viewPager.currentItem += 1
            }
        }
    }

    private fun storeResult(date: String, userAnswers: MutableSet<UserAnswer>) {
        testViewModel.storeResults(date, args.subject, userAnswers.toList())
        testViewModel.isStoredMutableLiveData.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBarTest.visibility = View.GONE
                findNavController().navigate(
                    TestFragmentDirections.actionTestFragmentToTestResultFragment(
                        userAnswers.toTypedArray(), date, args.subject.title
                    )
                )
            } else {
                binding.progressBarTest.visibility = View.GONE
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            alertDialogBuilder.apply {
                setTitle(getString(R.string.leave_test))
                setCancelable(true)
                setMessage(getString(R.string.leave_test_msg))
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    findNavController().navigateUp()
                }
                setNegativeButton(getString(R.string.no)) { _, _ -> }
            }.create().show()
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.notification)
        val descriptionText = getString(R.string.result)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(getString(R.string.app_name), name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun setNotification(date: String, userAnswers: MutableSet<UserAnswer>) {
        val pendingIntent = NavDeepLinkBuilder(requireActivity())
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.testResultFragment)
            .setArguments(
                TestResultFragmentArgs(
                    userAnswers.toTypedArray(),
                    date,
                    args.subject.title
                ).toBundle()
            )
            .createPendingIntent()

        notificationBuilder =
            NotificationCompat.Builder(requireActivity(), getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_book)
                .setContentTitle("${args.subject.title} Test Completed")
                .setContentText("On $date...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        with(NotificationManagerCompat.from(requireActivity())) {
            notify(1, notificationBuilder.build())
        }
    }
}