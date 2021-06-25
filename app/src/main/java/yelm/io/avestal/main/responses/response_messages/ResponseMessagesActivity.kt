package yelm.io.avestal.main.responses.response_messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import yelm.io.avestal.databinding.ActivityResponseMessagesBinding
import yelm.io.avestal.rest.responses.service.ServiceData
import yelm.io.avestal.rest.responses.user_responses.UserResponse

class ResponseMessagesActivity : AppCompatActivity() {
    lateinit var binding: ActivityResponseMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResponseMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userResponse = intent.extras?.get(UserResponse::class.java.name) as UserResponse
        binding.resyclerMessages.adapter =
            ResponseMessagesAdapter(userResponse.messages, this@ResponseMessagesActivity)

        binding.title.text = userResponse.service.title

        binding.back.setOnClickListener {
            finish()
        }
    }
}