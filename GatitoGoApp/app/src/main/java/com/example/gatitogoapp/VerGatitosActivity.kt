package com.example.gatitogoapp

import android.content.Intent
import android.os.Bundle
import androidx.cardview.widget.CardView;  // ✅ Esto es correcto
import androidx.appcompat.app.AppCompatActivity
import com.example.gatitogoapp.ui.SolicitarActivity

class VerGatitosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_gatitos)

        // Lista de gatos
        val gatitos = arrayOf<Gatito>(
            Gatito(
                R.id.buttonGatito1,
                "Canela",
                R.drawable.canela,
                "Edad: 3 meses\nDescripción: Naranja, muy cariñosa.\nCuidados: Requiere cepillado semanal."
            ),
            Gatito(
                R.id.buttonGatito2,
                "Rayitas",
                R.drawable.rayitas,
                "Edad: 4 meses\nDescripción: Atigrado, juguetón.\nCuidados: Requiere juguetes interactivos."
            ),
            Gatito(
                R.id.buttonGatito3,
                "Carboncito",
                R.drawable.carboncito,
                "Edad: 5 meses\nDescripción: Negro, muy tranquilo.\nCuidados: Requiere alimentación especial."
            ),
            Gatito(
                R.id.buttonGatito4,
                "Garfield",
                R.drawable.garfield,
                "Edad: 2 años\nDescripción: Naranja, dormilón.\nCuidados: Dieta controlada."
            ),
            Gatito(
                R.id.buttonGatito5,
                "Luna",
                R.drawable.luna,
                "Edad: 6 meses\nDescripción: Blanca y naranja, activa.\nCuidados: Necesita espacio para correr."
            ),
            Gatito(
                R.id.buttonGatito6,
                "Manchitas",
                R.drawable.manchitas,
                "Edad: 1 año\nDescripción: Manchada, cariñosa.\nCuidados: Ideal para familias."
            )
        )

        // Asigna eventos a los botones de cada gatito
        for (gatito in gatitos) {
            val button: CardView = findViewById(gatito.idButton)
            button.setOnClickListener { v -> abrirDetallesGatito(gatito) }
        }
    }

    private fun abrirDetallesGatito(gatito: Gatito) {
        val intent = Intent(
            this,
            SolicitarActivity::class.java
        )
        intent.putExtra("gatitoNombre", gatito.nombre)
        intent.putExtra("gatitoImagen", gatito.imagen)
        intent.putExtra("gatitoDetalles", gatito.detalles)
        startActivity(intent)
    }

    // Clase auxiliar para estructurar los datos de cada gatito
    internal inner class Gatito(
        var idButton: Int,
        var nombre: String,
        var imagen: Int,
        var detalles: String
    )
}