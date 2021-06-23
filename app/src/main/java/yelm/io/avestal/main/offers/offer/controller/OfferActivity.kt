package yelm.io.avestal.main.offers.offer.controller

import android.content.Intent
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import yelm.io.avestal.databinding.ActivityOfferBinding
import yelm.io.avestal.main.offers.offer.adapter.OfferImagesAdapter
import yelm.io.avestal.main.offers.offer_materials.ChooseMaterialActivity
import yelm.io.avestal.rest.responses.service.ServiceData
import java.text.ParseException
import java.util.*


class OfferActivity : AppCompatActivity() {

    lateinit var binding: ActivityOfferBinding
    private lateinit var mapObjects: MapObjectCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(getString(R.string.yandex_maps_API_key))
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val offer = intent.extras?.get(ServiceData::class.java.name) as ServiceData

        initMap(offer)
        initViews(offer)
        initActions()
        binding.respond.setOnClickListener {
            val intent = Intent(this@OfferActivity, ChooseMaterialActivity::class.java)
            intent.putExtra(ServiceData::class.java.name, offer)
            startActivity(intent)
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun initMap(service: ServiceData) {
        mapObjects = binding.mapView.map.mapObjects.addCollection()

        binding.mapView.map.isScrollGesturesEnabled = false
        binding.mapView.map.isZoomGesturesEnabled = false
        binding.mapView.map.isFastTapEnabled = false

        binding.mapView.map.move(
            CameraPosition(
                Point(
                    service.geolocation[0],
                    service.geolocation[1]
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
                    service.geolocation[0],
                    service.geolocation[1]
                )
            )

        mark.setIcon(
            ImageProvider.fromResource(
                this@OfferActivity,
                R.drawable.mark
            ),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(0.1f)
        )


    }

    private fun initViews(service: ServiceData) {
        binding.title.text = service.title
        binding.offerDescription.text = service.text

        val formattedPrice = priceFormat.format(service.price.toDouble())
        (getString(R.string.before) + " " + formattedPrice + " " +
                getString(R.string.ruble)).also { binding.price.text = it }

        binding.address.text = service.address

        binding.recyclerImage.addItemDecoration(
            ItemOffsetDecoration(
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_8dp).toInt()
            )
        )
        binding.recyclerImage.adapter = OfferImagesAdapter(service.images, this)

        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(service.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        binding.date.text = printedFormatterDateOfferActivity.format(currentCalendar.time)

        fillLayoutOfferDescription(service)


    }

    private fun fillLayoutOfferDescription(service: ServiceData) {
        for (file in service.files) {
            val textView = TextView(this)
            textView.text = file
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorBDBDBD))
            textView.includeFontPadding = false
            textView.compoundDrawablePadding = resources.getDimension(R.dimen.dimens_6dp).toInt()
            textView.isSingleLine = true
            textView.typeface = ResourcesCompat.getFont(this, R.font.golos_regular)
            textView.ellipsize = TextUtils.TruncateAt.END
            textView.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(this, R.drawable.ic_attachment),
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

    private fun initActions() {
        binding.back.setOnClickListener {
            finish()
        }
        binding.respond.setOnClickListener {

        }
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }
}