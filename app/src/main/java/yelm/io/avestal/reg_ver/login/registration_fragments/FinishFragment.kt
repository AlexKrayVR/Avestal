package yelm.io.avestal.reg_ver.login.registration_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import yelm.io.avestal.databinding.FragmentFinishBinding
import yelm.io.avestal.reg_ver.host.HostRegistration


class FinishFragment : Fragment() {

    private var binding: FragmentFinishBinding? = null
    private var hostRegistration: HostRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.close?.setOnClickListener {
            hostRegistration?.startApp()
        }
        binding?.registrationFinished?.setOnClickListener {
            hostRegistration?.startApp()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FinishFragment().apply {

            }
    }
    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostRegistration) {
            hostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }


}