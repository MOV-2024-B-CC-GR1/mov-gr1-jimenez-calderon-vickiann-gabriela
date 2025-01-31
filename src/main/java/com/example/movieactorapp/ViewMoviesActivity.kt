package com.example.movieactorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.adapters.MovieAdapter
import com.example.movieactorapp.models.Movie

class ViewMoviesActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var movieListView: ListView
    private lateinit var movieAdapter: MovieAdapter
    private val movies = mutableListOf<Movie>()

    companion object {
        const val UPDATE_MOVIE_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movies)

        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Configurar ListView
        movieListView = findViewById(R.id.movieListView)

        // Configurar adaptador
        movieAdapter = MovieAdapter(
            this,
            movies,
            onEditMovie = { movie ->
                val intent = Intent(this, UpdateMovieActivity::class.java)
                intent.putExtra("movie", movie)
                startActivityForResult(intent, UPDATE_MOVIE_REQUEST)
            },
            onDeleteMovie = { movie ->
                deleteMovie(movie)
            }
        )
        movieListView.adapter = movieAdapter

        // Cargar películas desde la base de datos
        loadMoviesFromDatabase()
    }

    private fun loadMoviesFromDatabase() {
        movies.clear()
        movies.addAll(databaseHelper.getAllMovies())
        movieAdapter.notifyDataSetChanged() // Asegura que el ListView refleje los datos actualizados
    }

    private fun deleteMovie(movie: Movie) {
        val rowsDeleted = databaseHelper.deleteMovie(movie.id.toLong())
        if (rowsDeleted > 0) {
            movies.remove(movie)
            movieAdapter.notifyDataSetChanged()
            Log.d("ViewMoviesActivity", "Película eliminada correctamente.")
        } else {
            Log.e("ViewMoviesActivity", "No se pudo eliminar la película.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_MOVIE_REQUEST && resultCode == RESULT_OK) {
            val updatedMovie = data?.getParcelableExtra<Movie>("updatedMovie")
            updatedMovie?.let {
                // Encuentra y actualiza la película en la lista
                val index = movies.indexOfFirst { it.id == updatedMovie.id }
                if (index != -1) {
                    movies[index] = updatedMovie
                    movieAdapter.notifyDataSetChanged() // Notifica al adaptador de los cambios
                } else {
                    // Si no se encuentra, recargar la lista desde la base de datos
                    loadMoviesFromDatabase()
                }
            }
        }
    }
}
