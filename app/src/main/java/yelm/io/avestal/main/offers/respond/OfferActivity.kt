package yelm.io.avestal.main.offers.respond

import android.content.Intent
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
import com.yandex.mapkit.map.CameraPosition
import yelm.io.avestal.R
import yelm.io.avestal.common.ItemOffsetDecoration
import yelm.io.avestal.common.priceFormat
import yelm.io.avestal.common.printedFormatterDateOfferActivity
import yelm.io.avestal.common.serverFormatterDate
import yelm.io.avestal.databinding.ActivityOfferBinding
import yelm.io.avestal.main.offers.respond.adapter.OfferImagesAdapter
import yelm.io.avestal.rest.responses.OfferData
import java.text.ParseException
import java.util.*


class OfferActivity : AppCompatActivity() {

    lateinit var binding: ActivityOfferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(getString(R.string.yandex_maps_API_key))
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val offer = intent.extras?.get(OfferData::class.java.name) as OfferData

        initMap(offer)
        initViews(offer)
        initActions()


    }

    private fun initMap(offer: OfferData) {
        binding.mapView.map.isScrollGesturesEnabled = false
        binding.mapView.map.isZoomGesturesEnabled = false
        binding.mapView.map.isFastTapEnabled = false

        binding.mapView.map.move(
            CameraPosition(
                Point(
                    offer.geolocation[0],
                    offer.geolocation[1]
                ),
                16f,
                0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
    }

    private fun initViews(offer: OfferData) {
        binding.title.text = offer.title
        binding.offerDescription.text = offer.text

        val formattedPrice = priceFormat.format(offer.price.toInt())
        (getString(R.string.before) + " " + formattedPrice + " " +
                getString(R.string.ruble)).also { binding.price.text = it }

        binding.address.text = offer.address

        binding.recyclerImage.addItemDecoration(
            ItemOffsetDecoration(
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_8dp).toInt()
            )
        )
        binding.recyclerImage.adapter = OfferImagesAdapter(offer.images, this)

        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(serverFormatterDate.parse(offer.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        binding.date.text = printedFormatterDateOfferActivity.format(currentCalendar.time)

        fillLayoutOfferDescription(offer)
    }

    private fun fillLayoutOfferDescription(offer: OfferData) {
        for (file in offer.files) {
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