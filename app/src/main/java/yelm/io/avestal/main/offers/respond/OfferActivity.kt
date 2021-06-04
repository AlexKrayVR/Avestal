package yelm.io.avestal.main.offers.respond

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import yelm.io.avestal.R
import yelm.io.avestal.common.ItemOffsetDecoration
import yelm.io.avestal.common.currentFormatterDate
import yelm.io.avestal.common.printedFormatterDateOfferActivity
import yelm.io.avestal.databinding.ActivityOfferBinding
import yelm.io.avestal.main.offers.respond.adapter.OfferImagesAdapter
import yelm.io.avestal.rest.responses.OfferData
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

class OfferActivity : AppCompatActivity() {

    lateinit var binding: ActivityOfferBinding
    private val DEFAULT_ZOOM = 16f

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(getString(R.string.yandex_maps_API_key))
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.map.isScrollGesturesEnabled = false
        binding.mapView.map.isZoomGesturesEnabled = false
        binding.mapView.map.isFastTapEnabled = false

        val offer = intent.extras?.get(OfferData::class.java.name) as OfferData

        binding.title.text = offer.title
        binding.offerDescription.text = offer.text
        binding.offerDocx.text = offer.files[0]

        val formattedPrice = DecimalFormat("###,###").format(offer.price.toInt())
        (getString(R.string.before) + " " + formattedPrice + " " +
                getString(R.string.ruble)).also { binding.price.text = it }

        binding.back.setOnClickListener {
            finish()
        }

        binding.address.text = offer.address


        binding.recyclerImage.addItemDecoration(
            ItemOffsetDecoration(
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_16dp).toInt(),
                resources.getDimension(R.dimen.dimens_8dp).toInt()
            )
        )
        binding.recyclerImage.adapter= OfferImagesAdapter(offer.images, this)


        val currentCalendar = GregorianCalendar.getInstance()
        try {
            currentCalendar.time =
                Objects.requireNonNull(currentFormatterDate.parse(offer.updatedAt))
        } catch (e: ParseException) {
            e.printStackTrace();
        }

        binding.date.text = printedFormatterDateOfferActivity.format(currentCalendar.time)

        binding.save.setOnClickListener {

        }

        binding.mapView.map.move(
            CameraPosition(
                Point(
                    offer.geolocation[0].toDouble(),
                    offer.geolocation[1].toDouble()
                ),
                16f,
                0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )




    }




    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }


}