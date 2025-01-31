package com.example.movieactorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.example.movieactorapp.R
import com.example.movieactorapp.models.Actor

class ActorAdapter(
    private val context: Context,
    private val actors: List<Actor>
) : ArrayAdapter<Actor>(context, 0, actors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.actor_item, parent, false)

        val actor = actors[position]

        // Vincular vistas
        val nameTextView: TextView = view.findViewById(R.id.actorNameTextView)
        val ageTextView: TextView = view.findViewById(R.id.actorAgeTextView)
        val nationalityTextView: TextView = view.findViewById(R.id.actorNationalityTextView)
        val oscarWinnerTextView: TextView = view.findViewById(R.id.actorOscarWinnerTextView)
        val salaryTextView: TextView = view.findViewById(R.id.actorSalaryTextView)

        // Configurar valores
        nameTextView.text = actor.name
        ageTextView.text = "Edad: ${actor.age}"
        nationalityTextView.text = "Nacionalidad: ${actor.nationality}"
        oscarWinnerTextView.text = if (actor.isOscarWinner) "Ganador del Oscar" else "No ganador del Oscar"
        salaryTextView.text = "Salario: ${actor.salary}"

        return view
    }
}


