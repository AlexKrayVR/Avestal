package yelm.io.avestal.main.user.settings.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yelm.io.avestal.databinding.FragmentSettingsBinding
import yelm.io.avestal.main.user.settings.HostSettings


class SettingsFragment : Fragment() {

    private var hostSettings: HostSettings? = null

    private var binding: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.logout?.setOnClickListener {
            hostSettings?.logOut()
        }
        binding?.back?.setOnClickListener {
            hostSettings?.finishActivity()
        }

        binding?.layoutSettingsChat?.setOnClickListener {
            hostSettings?.openChatNotificationFragment()
        }
        binding?.layoutSettingsSuitableOrderNotification?.setOnClickListener {
            hostSettings?.openOrderNotificationFragment()
        }
        binding?.layoutSettingsUserPhone?.setOnClickListener {
            hostSettings?.openPhoneFragment("333")
        }
        binding?.layoutSettingsUserMail?.setOnClickListener {
            hostSettings?.openMailFragment()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
            }
    }

    override fun onDetach() {
        super.onDetach()
        hostSettings = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostSettings) {
            hostSettings = activity as HostSettings
        } else {
            throw RuntimeException(activity.toString() + " must implement HostSettings interface")
        }
    }
}