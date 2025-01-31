package com.example.movieactorapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.models.Movie

class UpdateMovieActivity : AppCompatActivity() {
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_movie)

        val titleEditText: EditText = findViewById(R.id.movieTitleEditText)
        val genreEditText: EditText = findViewById(R.id.movieGenreEditText)
        val directorEditText: EditText = findViewById(R.id.movieDirectorEditText)
        val durationEditText: EditText = findViewById(R.id.movieDurationEditText)
        val releaseDateEditText: EditText = findViewById(R.id.movieReleaseDateEditText)
        val updateButton: Button = findViewById(R.id.updateMovieButton)


        // Verificar si se recibió un objeto Movie
        movie = intent.getParcelableExtra("movie")
        movie?.let {
            titleEditText.setText(it.title)
            genreEditText.setText(it.genre)
            directorEditText.setText(it.director)
            durationEditText.setText(it.duration.toString())
            releaseDateEditText.setText(it.releaseDate)

        }

        // Manejar el clic del botón Actualizar
        updateButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedGenre = genreEditText.text.toString()
            val updatedDirector = directorEditText.text.toString()
            val updatedDuration = durationEditText.text.toString().toIntOrNull() ?: 0
            val updatedDate = releaseDateEditText.text.toString()

            if (movie != null) {
                movie?.apply {
                    title = updatedTitle
                    genre = updatedGenre
                    director = updatedDirector
                    duration = updatedDuration
                    releaseDate = updatedDate
                }

                val databaseHelper = DatabaseHelper(this)
                val rowsUpdated = databaseHelper.updateMovie(movie!!)
                if (rowsUpdated > 0) {
                    // Devolver los datos actualizados
                    val resultIntent = Intent()
                    resultIntent.putExtra("updatedMovie", movie)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar la película", Toast.LENGTH_SHORT).show()
                }

            finish() // Finalizar la actividad
        }
    }
}}
