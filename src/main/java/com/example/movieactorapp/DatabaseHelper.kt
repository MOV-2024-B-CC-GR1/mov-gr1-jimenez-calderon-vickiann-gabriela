package com.example.movieactorapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.movieactorapp.models.toContentValues
import com.example.movieactorapp.models.Actor
import com.example.movieactorapp.models.Movie



class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla de actores
        db.execSQL(
            "CREATE TABLE $TABLE_ACTORS (" +
                    "$COLUMN_ACTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ACTOR_NAME TEXT NOT NULL, " +
                    "$COLUMN_ACTOR_AGE INTEGER NOT NULL, " +
                    "$COLUMN_ACTOR_NATIONALITY TEXT, " +
                    "$COLUMN_ACTOR_IS_OSCAR_WINNER INTEGER NOT NULL, " +
                    "$COLUMN_ACTOR_SALARY REAL NOT NULL)"
        )

        // Crear tabla de películas
        db.execSQL(
            "CREATE TABLE $TABLE_MOVIES (" +
                    "$COLUMN_MOVIE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_MOVIE_TITLE TEXT NOT NULL, " +
                    "$COLUMN_MOVIE_GENRE TEXT NOT NULL, " +
                    "$COLUMN_MOVIE_DIRECTOR TEXT, " +
                    "$COLUMN_MOVIE_DURATION INTEGER, " +
                    "$COLUMN_MOVIE_RELEASE_DATE TEXT)"
        )

        // Crear tabla intermedia (relación entre actores y películas)
        db.execSQL(
            "CREATE TABLE $TABLE_MOVIE_ACTORS (" +
                    "$COLUMN_MOVIE_ACTOR_MOVIE_ID INTEGER NOT NULL, " +
                    "$COLUMN_MOVIE_ACTOR_ACTOR_ID INTEGER NOT NULL, " +
                    "PRIMARY KEY ($COLUMN_MOVIE_ACTOR_MOVIE_ID, $COLUMN_MOVIE_ACTOR_ACTOR_ID), " +
                    "FOREIGN KEY ($COLUMN_MOVIE_ACTOR_MOVIE_ID) REFERENCES $TABLE_MOVIES($COLUMN_MOVIE_ID), " +
                    "FOREIGN KEY ($COLUMN_MOVIE_ACTOR_ACTOR_ID) REFERENCES $TABLE_ACTORS($COLUMN_ACTOR_ID))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_MOVIES ADD COLUMN $COLUMN_MOVIE_RELEASE_DATE TEXT")
        }
        onCreate(db)
    }

    // Métodos CRUD para actores
    fun addActor(actor: Actor): Long {
        val db = writableDatabase
        val values = actor.toContentValues()
         return db.insert(TABLE_ACTORS, null, values)
    }

    fun getAllActors(): List<Actor> {
        val actors = mutableListOf<Actor>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ACTORS", null)
        if (cursor.moveToFirst()) {
            do {
                actors.add(
                    Actor(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_NAME)),
                        age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_AGE)),
                        nationality = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_NATIONALITY)),
                        isOscarWinner = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_IS_OSCAR_WINNER)) == 1,
                        salary = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_SALARY))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return actors
    }

    fun updateActor(actorId: Long, values: ContentValues): Int {
        val db = writableDatabase
        return db.update(TABLE_ACTORS, values, "$COLUMN_ACTOR_ID=?", arrayOf(actorId.toString()))
    }

    fun deleteActor(actorId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_ACTORS, "$COLUMN_ACTOR_ID=?", arrayOf(actorId.toString()))
    }

    // Métodos CRUD para películas
    fun addMovie(movie: Movie): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MOVIE_TITLE, movie.title)
            put(COLUMN_MOVIE_GENRE, movie.genre)
            put(COLUMN_MOVIE_DIRECTOR, movie.director)
            put(COLUMN_MOVIE_DURATION, movie.duration)
            put(COLUMN_MOVIE_RELEASE_DATE, movie.releaseDate)
        }
        return db.insert(TABLE_MOVIES, null, values)
    }

    fun getAllMovies(): List<Movie> {
        val movies = mutableListOf<Movie>()
        val db = readableDatabase

        // Obtener todas las películas
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MOVIES", null)
        if (cursor.moveToFirst()) {
            do {
                val movieId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_TITLE))
                val genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_GENRE))
                val director = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_DIRECTOR))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_DURATION))
                val releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_RELEASE_DATE))

                // Obtener actores asociados a esta película
                val actorCursor = db.rawQuery(
                    "SELECT * FROM $TABLE_ACTORS " +
                            "INNER JOIN $TABLE_MOVIE_ACTORS ON $TABLE_ACTORS.$COLUMN_ACTOR_ID = $TABLE_MOVIE_ACTORS.$COLUMN_MOVIE_ACTOR_ACTOR_ID " +
                            "WHERE $TABLE_MOVIE_ACTORS.$COLUMN_MOVIE_ACTOR_MOVIE_ID = ?",
                    arrayOf(movieId.toString())
                )

                val actors = mutableListOf<Actor>()
                if (actorCursor.moveToFirst()) {
                    do {
                        actors.add(
                            Actor(
                                id = actorCursor.getInt(actorCursor.getColumnIndexOrThrow(COLUMN_ACTOR_ID)),
                                name = actorCursor.getString(actorCursor.getColumnIndexOrThrow(COLUMN_ACTOR_NAME)),
                                age = actorCursor.getInt(actorCursor.getColumnIndexOrThrow(COLUMN_ACTOR_AGE)),
                                nationality = actorCursor.getString(actorCursor.getColumnIndexOrThrow(COLUMN_ACTOR_NATIONALITY)),
                                isOscarWinner = actorCursor.getInt(actorCursor.getColumnIndexOrThrow(COLUMN_ACTOR_IS_OSCAR_WINNER)) == 1,
                                salary = actorCursor.getDouble(actorCursor.getColumnIndexOrThrow(COLUMN_ACTOR_SALARY))
                            )
                        )
                    } while (actorCursor.moveToNext())
                }
                actorCursor.close()

                // Crear el objeto Movie con actores asociados
                movies.add(Movie(movieId, title, genre, director, duration, releaseDate, actors))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return movies
    }


    fun deleteMovie(movieId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_MOVIES, "$COLUMN_MOVIE_ID=?", arrayOf(movieId.toString()))
    }

    fun updateMovie(movie: Movie): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MOVIE_TITLE, movie.title)
            put(COLUMN_MOVIE_GENRE, movie.genre)
            put(COLUMN_MOVIE_DIRECTOR, movie.director)
            put(COLUMN_MOVIE_DURATION, movie.duration)
            put(COLUMN_MOVIE_RELEASE_DATE, movie.releaseDate)
        }

        // Actualizar la película por ID
        return db.update(
            TABLE_MOVIES,
            values,
            "$COLUMN_MOVIE_ID = ?",
            arrayOf(movie.id.toString())
        )
    }


    // Relación película-actores
    fun getActorsForMovie(movieId: Long): List<Actor> {
        val actors = mutableListOf<Actor>()
        val db = readableDatabase
        val query = """
        SELECT a.* FROM $TABLE_ACTORS a
        INNER JOIN $TABLE_MOVIE_ACTORS ma ON a.$COLUMN_ACTOR_ID = ma.$COLUMN_MOVIE_ACTOR_ACTOR_ID
        WHERE ma.$COLUMN_MOVIE_ACTOR_MOVIE_ID = ?
    """
        val cursor = db.rawQuery(query, arrayOf(movieId.toString()))
        if (cursor.moveToFirst()) {
            do {
                actors.add(
                    Actor(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_NAME)),
                        age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_AGE)),
                        nationality = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_NATIONALITY)),
                        isOscarWinner = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_IS_OSCAR_WINNER)) == 1,
                        salary = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACTOR_SALARY))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return actors
    }

    fun linkActorToMovie(movieId: Long, actorId: Long): Long {
        val db = writableDatabase
        val query = """
        SELECT 1 FROM $TABLE_MOVIE_ACTORS
        WHERE $COLUMN_MOVIE_ACTOR_MOVIE_ID = ? AND $COLUMN_MOVIE_ACTOR_ACTOR_ID = ?
    """
        val cursor = db.rawQuery(query, arrayOf(movieId.toString(), actorId.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()

        return if (!exists) {
            val values = ContentValues().apply {
                put(COLUMN_MOVIE_ACTOR_MOVIE_ID, movieId)
                put(COLUMN_MOVIE_ACTOR_ACTOR_ID, actorId)
            }
            db.insert(TABLE_MOVIE_ACTORS, null, values)
        } else {
            -1 // Ya existe la relación
        }
    }


    fun getMoviesWithActors(): List<Movie> {
        val movies = getAllMovies()
        movies.forEach { movie ->
            movie.actors = getActorsForMovie(movie.id.toLong())
        }
        return movies
    }



    companion object {
        const val DATABASE_NAME = "movie_actor_app.db"
        const val DATABASE_VERSION = 2

        const val TABLE_ACTORS = "actors"
        const val COLUMN_ACTOR_ID = "id"
        const val COLUMN_ACTOR_NAME = "name"
        const val COLUMN_ACTOR_AGE = "age"
        const val COLUMN_ACTOR_NATIONALITY = "nationality"
        const val COLUMN_ACTOR_IS_OSCAR_WINNER = "is_oscar_winner"
        const val COLUMN_ACTOR_SALARY = "salary"

        const val TABLE_MOVIES = "movies"
        const val COLUMN_MOVIE_ID = "id"
        const val COLUMN_MOVIE_TITLE = "title"
        const val COLUMN_MOVIE_GENRE = "genre"
        const val COLUMN_MOVIE_DIRECTOR = "director"
        const val COLUMN_MOVIE_DURATION = "duration"
        const val COLUMN_MOVIE_RELEASE_DATE = "release_date"

        const val TABLE_MOVIE_ACTORS = "movie_actors"
        const val COLUMN_MOVIE_ACTOR_MOVIE_ID = "movie_id"
        const val COLUMN_MOVIE_ACTOR_ACTOR_ID = "actor_id"
    }
}
