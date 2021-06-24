package yelm.io.avestal.main.services.service.fragments

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.runtime.image.ImageProvider
import yelm.io.avestal.R
import yelm.io.avestal.common.ItemOffsetDecoration
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.common.printedFormatterDateOfferActivity
import yelm.io.avestal.common.serverFormatterDate
import yelm.io.avestal.databinding.FragmentServiceInfoBinding
import yelm.io.avestal.main.services.service.host.HostService
import yelm.io.avestal.main.services.service.adapters.OfferImagesAdapter
import yelm.io.avestal.rest.responses.service.ServiceData
import java.lang.RuntimeException
import java.text.ParseException
import java.util.*

class ServiceInfoFragment : Fragment() {
    private lateinit var serviceData: ServiceData
    private var _binding: FragmentServiceInfoBinding? = null
    private val binding get() = _binding!!

    private var hostService: HostService? = null
    private lateinit var mapObjects: MapObjectCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(getString(R.string.yandex_maps_API_key))
        MapKitFactory.initialize(requireContext())
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceData = it.get(ServiceData::class.java.name) as ServiceData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceInfoBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()

        initViews()
        fillLayoutOfferDescription()

        binding.respond.setOnClickListener {
            hostService?.openServiceMaterials()
        }
        binding.back.setOnClickListener {
            hostService?.back()
        }
    }

    private fun initMap() {
        mapObjects = binding.mapView.map.mapObjects.addCollection()

        binding.mapView.map.isScrollGesturesEnabled = false
        binding.mapView.map.isZoomGesturesEnabled = false
        binding.mapView.map.isFastTapEnabled = false

        binding.mapView.map.move(
            CameraPosition(
                Point(
                    serviceData.geolocation[0],
                    serviceData.geolocation[1]
                ),
                16f,
                0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        val mark: PlacemarkMapObject = mapObjects
            .addEmptyPlacemark(
                Point(
                    serviceData.geolocation[0],
                    serviceData.geolocation[1]
                )
            )

        mark.setIcon(
            ImageProvider.fromResource(
                requireContext(),
                R.drawable.mark
            ),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(0.1f)
        )
    }

    private fun initViews() {
        binding.title.text = serviceData.title
        binding.offerDescription.text = serviceData.text

        val formattedPrice = priceFormat.format(serviceData.price.toDouble())
        (getString(R.string.before) + " " + formattedPrice + " " +
                getString(R.string.ruble)).also { binding.price.text = it }

        binding.address.text = serviceData.address

        binding.recyclerImage.addItemDecoration(
            ItemOffsetDecoration(
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_8dp).toInt()
            )
        )
        binding.recyclerImage.adapter = OfferImagesAdapter(serviceData.images, requireContext())

        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(serviceData.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        _binding?.date?.text = printedFormatterDateOfferActivity.format(currentCalendar.time)
    }

    private fun fillLayoutOfferDescription() {
        for (file in serviceData.files) {
            val textView = TextView(requireContext())
            textView.text = file
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBDBDBD))
            textView.includeFontPadding = false
            textView.compoundDrawablePadding = resources.getDimension(R.dimen.dimens_6dp).toInt()
            textView.isSingleLine = true
            textView.typeface = ResourcesCompat.getFont(requireContext(), R.font.golos_regular)
            textView.ellipsize = TextUtils.TruncateAt.END
            textView.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_attachment),
                null,
                null,
                null
            )
            textView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(file))
                startActivity(intent)
            }
            binding.layoutOfferDescription.addView(textView)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(serviceData: ServiceData) =
            ServiceInfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ServiceData::class.java.name, serviceData)
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostService) {
            hostService = activity as HostService
        } else {
            throw RuntimeException(activity.toString() + " must implement HostService interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        hostService = null
    }

    override fun onStop() {
        _binding?.mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        _binding?.mapView?.onStart()
    }
}