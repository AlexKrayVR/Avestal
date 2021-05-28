package yelm.io.avestal.main.user.photo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import yelm.io.avestal.R
import yelm.io.avestal.databinding.ActivityPickPhotoBinding

class PickPhotoActivity : AppCompatActivity() {

    lateinit var binding: ActivityPickPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(AppCompatResources.getDrawable(this, R.drawable.ic_user_picture_holder))
            .transform(
                CenterCrop(), RoundedCorners(
                    resources.getDimension(R.dimen.dimens_16dp)
                        .toInt()
                )
            )
            .into(binding.userImage)

        binding.back.setOnClickListener {
            finish()
        }
    }
}