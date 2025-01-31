package com.example.movieactorapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.models.Movie

class MovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        // Obtén el objeto Movie del intent
        val movie = intent.getParcelableExtra<Movie>("movie")

        // Referencia las vistas
        val titleTextView: TextView = findViewById(R.id.movieTitleTextView)
        val genreTextView: TextView = findViewById(R.id.movieGenreTextView)
        val directorTextView: TextView = findViewById(R.id.movieDirectorTextView)
        val durationTextView: TextView = findViewById(R.id.movieDurationTextView)
        val releaseDateTextView: TextView = findViewById(R.id.movieReleaseDateTextView)

        // Llena las vistas con los detalles de la película
        movie?.let {
            titleTextView.text = it.title
            genreTextView.text = getString(R.string.genre_format, it.genre)
            directorTextView.text = getString(R.string.director_format, it.director)
            durationTextView.text = getString(R.string.duration_format, it.duration)
            releaseDateTextView.text = getString(R.string.release_date_format, it.releaseDate)
        }
    }
}
