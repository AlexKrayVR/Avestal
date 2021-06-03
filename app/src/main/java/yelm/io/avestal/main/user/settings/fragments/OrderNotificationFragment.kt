package yelm.io.avestal.main.user.settings.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import yelm.io.avestal.databinding.FragmentOrderNotificationBinding
import yelm.io.avestal.main.user.settings.HostSettings

class OrderNotificationFragment : Fragment() {

    private var binding: FragmentOrderNotificationBinding? = null
    private var hostSettings: HostSettings? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderNotificationBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.back?.setOnClickListener {
            hostSettings?.back()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OrderNotificationFragment().apply {
                arguments = Bundle().apply {

                }
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