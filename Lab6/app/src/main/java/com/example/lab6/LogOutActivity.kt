package com.example.lab6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_log_out.*

class LogOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)

        val bundle = intent.extras
        if (bundle != null) {
            text_view_user_name.text = bundle.getString(resources.getString(R.string.user_name_label))
            text_view_user_email.text = bundle.getString(resources.getString(R.string.user_email_label))
        }

        sign_out_button.setOnClickListener{
            finish()
        }

    }
}