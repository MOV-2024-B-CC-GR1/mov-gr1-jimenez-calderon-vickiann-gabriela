package com.example.movieactorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.BaseAdapter
import com.example.movieactorapp.R
import com.example.movieactorapp.models.Movie

class MovieAdapter(
    private val context: Context,
    private val movies: List<Movie>,
    private val onEditMovie: (Movie) -> Unit,
    private val onDeleteMovie: (Movie) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = movies.size

    override fun getItem(position: Int): Any = movies[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        val movie = movies[position]

        // Vincular vistas
        val titleTextView: TextView = view.findViewById(R.id.movieTitleTextView)
        val genreTextView: TextView = view.findViewById(R.id.movieGenreTextView)
        val releaseDateTextView: TextView = view.findViewById(R.id.movieReleaseDateTextView)
        val deleteButton: Button = view.findViewById(R.id.deleteMovieButton)
        val actorsTextView: TextView = view.findViewById(R.id.movieActors)

        // Configurar valores
        titleTextView.text = movie.title
        genreTextView.text = movie.genre
        releaseDateTextView.text = movie.releaseDate
        // Obtener y mostrar los actores
        val actorNames = movie.actors.joinToString(", ") { it.name }
        actorsTextView.text = "Actors: $actorNames"
        val movieActorsTextView: TextView = view.findViewById(R.id.movieActors)
        movieActorsTextView.text = "Actores: " + movie.actors.joinToString { it.name }


        // Botón "EDITAR"
        val editButton: Button = view.findViewById(R.id.editMovieButton)
        editButton.setOnClickListener {
            onEditMovie(movie)
        }

        // Configurar el botón "Eliminar"
        deleteButton.setOnClickListener {
            onDeleteMovie(movie)
        }

        return view
    }
}


