package com.example.gatitogoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonComenzar = findViewById<Button>(R.id.buttonComenzar)
        buttonComenzar.setOnClickListener {
            val intent = Intent(this, VerGatitosActivity::class.java)
            startActivity(intent)
        }

        val btnVerSolicitudes = findViewById<Button>(R.id.btnVerSolicitudes)
        btnVerSolicitudes.setOnClickListener {
            val intent = Intent(this, ListaSolicitudesActivity::class.java)
            startActivity(intent)
        }
    }
}
