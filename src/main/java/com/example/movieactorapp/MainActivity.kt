package com.example.movieactorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.adapters.ActorAdapter
import com.example.movieactorapp.adapters.MovieAdapter
import com.example.movieactorapp.models.Actor
import com.example.movieactorapp.models.Movie

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var actorAdapter: ActorAdapter

    private val movies = mutableListOf<Movie>() // Lista de películas en memoria
    private val actors = mutableListOf<Actor>() // Lista de actores en memoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Inicializar adaptadores
        movieAdapter = MovieAdapter(this, movies, onEditMovie = { movie ->
            // Lógica para editar película
            val intent = Intent(this, UpdateMovieActivity::class.java)
            intent.putExtra("movie", movie)
            startActivity(intent)
        }, onDeleteMovie = { movie ->
            deleteMovie(movie.id)
        })

        actorAdapter = ActorAdapter(this, actors)


        // Cargar datos iniciales desde la base de datos
        loadMoviesFromDatabase()
        loadActorsFromDatabase()

        // Botones
        val createMovieButton: Button = findViewById(R.id.createMovieButton)
        val addActorButton: Button = findViewById(R.id.addActorButton)
        val viewMoviesButton: Button = findViewById(R.id.viewMoviesButton)
        val viewActorsButton: Button = findViewById(R.id.readActorsButton)

        createMovieButton.setOnClickListener {
            val intent = Intent(this, CreateMovieActivity::class.java)
            startActivity(intent)
        }

        addActorButton.setOnClickListener {
            val intent = Intent(this, AddActorActivity::class.java)
            startActivity(intent)
        }
        viewMoviesButton.setOnClickListener {
            val intent = Intent(this, ViewMoviesActivity::class.java)
            intent.putParcelableArrayListExtra("movies", ArrayList(movies)) // Envía la lista de películas
            startActivity(intent)
        }
        viewActorsButton.setOnClickListener {
            val intent = Intent(this, ViewActorsActivity::class.java)
            intent.putParcelableArrayListExtra("actors", ArrayList(actors)) // Envía la lista de actores
            startActivity(intent)
        }
    }

    private fun loadMoviesFromDatabase() {
        movies.clear()
        movies.addAll(databaseHelper.getAllMovies())
        movieAdapter.notifyDataSetChanged()
    }

    private fun loadActorsFromDatabase() {
        actors.clear()
        actors.addAll(databaseHelper.getAllActors())
        actorAdapter.notifyDataSetChanged()
    }

    private fun deleteMovie(movieId: Int) {
        val rowsDeleted = databaseHelper.deleteMovie(movieId.toLong())
        if (rowsDeleted > 0) {
            movies.removeIf { it.id == movieId }
            movieAdapter.notifyDataSetChanged()
        } else {
            Log.e("MainActivity", "No se pudo eliminar la película con ID $movieId")
        }
    }

    companion object {
        const val CREATE_MOVIE_REQUEST = 1
        const val UPDATE_MOVIE_REQUEST = 2
    }
}
