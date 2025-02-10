package com.example.gatitogoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.gatitogoapp.R

class ConfirmacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion)

        val buttonHome = findViewById<Button>(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this, VerGatitosActivity::class.java)
            startActivity(intent)
        }
    }
}
