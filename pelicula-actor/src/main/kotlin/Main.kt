package org.example
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

data class Pelicula(
    var nombre: String,
    var genero: String,
    var director: String,
    var duracion: Int,
    var fechaEstreno: Date,
    val actores: MutableList<Actor> = mutableListOf()
) : Serializable

data class Actor(
    var nombre: String,
    var edad: Int,
    var nacionalidad: String,
    var ganadorOscar: Boolean,
    var salario: Double
) : Serializable

// Archivo para datos y lista de películas
private const val FILE_NAME = "C:/Users/Sloth/OneDrive - Escuela Politécnica Nacional/Documentos/2024-B/Aplicaciones Móviles/mov-gr1-jimenez-calderon-vickiann-gabriela/pelicula-actor/peliculas.dat"
private var peliculas: MutableList<Pelicula> = mutableListOf()

fun main() {
    cargarDatos()
    val scanner = Scanner(System.`in`)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    while (true) {
        println("\n=== Menú Principal ===")
        println("1. Crear Película")
        println("2. Leer Películas")
        println("3. Actualizar Película")
        println("4. Eliminar Película")
        println("5. Leer Actores")
        println("6. Actualizar Actor")
        println("7. Eliminar Actor")
        println("8. Agregar Actor")
        println("9. Salir")
        print("Seleccione una opción: ")


        when (scanner.nextLine().toIntOrNull()) {
            1 -> crearPelicula(scanner, dateFormat)
            2 -> leerPeliculas()
            3 -> actualizarPelicula(scanner)
            4 -> eliminarPelicula(scanner)
            5 -> leerActores(scanner)
            6 -> actualizarActor(scanner)
            7 -> eliminarActor(scanner)
            8 -> agregarActor(scanner)
            9 -> {
                guardarDatos()
                println("¡Datos guardados y programa terminado!")
                break
            }
            else -> println("Opción no existente, intente nuevamente.")
        }
    }
}

// Crear una nueva película
private fun crearPelicula(scanner: Scanner, dateFormat: SimpleDateFormat) {
    println("\n=== Crear Película ===")
    val nombre = solicitarCadena("Nombre: ", scanner)
    val genero = solicitarCadena("Género: ", scanner)
    val director = solicitarCadena("Director: ", scanner)
    val duracion = solicitarEntero("Duración (minutos): ", scanner)
    val fechaEstreno = solicitarFecha("Fecha de Estreno (yyyy-MM-dd): ", scanner, dateFormat)

    val pelicula = Pelicula(nombre, genero, director, duracion, fechaEstreno)

    print("¿Cuántos actores desea añadir?: ")
    val numActores = scanner.nextLine().toIntOrNull() ?: 0
    for (i in 1..numActores) {
        println("\n=== Actor #$i ===")
        val actorNombre = solicitarCadena("Nombre: ", scanner)
        val edad = solicitarEntero("Edad: ", scanner)
        val nacionalidad = solicitarCadena("Nacionalidad: ", scanner)
        val ganadorOscar = solicitarBooleano("¿Ganador del Oscar? (true/false): ", scanner)
        val salario = solicitarDecimal("Salario: ", scanner)

        pelicula.actores.add(Actor(actorNombre, edad, nacionalidad, ganadorOscar, salario))
    }

    peliculas.add(pelicula)
    println("Película creada exitosamente.")
}

// Leer todas las películas
private fun leerPeliculas() {
    println("\n=== Lista de Películas ===")
    if (peliculas.isEmpty()) {
        println("No hay películas registradas.")
    } else {
        peliculas.forEachIndexed { index, pelicula ->
            println("\nPelicula #${index + 1}")
            println("Nombre: ${pelicula.nombre}")
            println("Género: ${pelicula.genero}")
            println("Director: ${pelicula.director}")
            println("Duración: ${pelicula.duracion} minutos")
            println("Fecha de Estreno: ${pelicula.fechaEstreno}")
            println("Actores:")
            pelicula.actores.forEach { actor ->
                println(" - $actor")
            }
        }
    }
}

// Actualizar una película existente
private fun actualizarPelicula(scanner: Scanner) {
    println("\n=== Actualizar Película ===")
    val nombre = solicitarCadena("Ingrese el nombre de la película a actualizar: ", scanner)
    val pelicula = peliculas.find { it.nombre.equals(nombre, ignoreCase = true) }

    if (pelicula != null) {
        pelicula.nombre = solicitarCadena("Nuevo Nombre (actual: ${pelicula.nombre}): ", scanner)
        pelicula.genero = solicitarCadena("Nuevo Género (actual: ${pelicula.genero}): ", scanner)
        pelicula.director = solicitarCadena("Nuevo Director (actual: ${pelicula.director}): ", scanner)
        pelicula.duracion = solicitarEntero("Nueva Duración (actual: ${pelicula.duracion}): ", scanner)
        println("Película actualizada exitosamente.")
    } else {
        println("Película no encontrada.")
    }
}

// Funciones para validar entradas

private fun solicitarCadena(mensaje: String, scanner: Scanner): String {
    while (true) {
        print(mensaje)
        val input = scanner.nextLine()
        if (input.isNotBlank()) return input
        println("Entrada no válida. Intente nuevamente.")
    }
}

private fun solicitarEntero(mensaje: String, scanner: Scanner): Int {
    while (true) {
        print(mensaje)
        val input = scanner.nextLine()
        val numero = input.toIntOrNull()
        if (numero != null) return numero
        println("Entrada no válida. Ingrese un número entero.")
    }
}

private fun solicitarDecimal(mensaje: String, scanner: Scanner): Double {
    while (true) {
        print(mensaje)
        val input = scanner.nextLine()
        val numero = input.toDoubleOrNull()
        if (numero != null) return numero
        println("Entrada no válida. Ingrese un número decimal.")
    }
}

private fun solicitarBooleano(mensaje: String, scanner: Scanner): Boolean {
    while (true) {
        print(mensaje)
        val input = scanner.nextLine().lowercase()
        if (input == "true" || input == "false") return input.toBoolean()
        println("Entrada no válida. Ingrese 'true' o 'false'.")
    }
}

private fun solicitarFecha(mensaje: String, scanner: Scanner, dateFormat: SimpleDateFormat): Date {
    while (true) {
        print(mensaje)
        val input = scanner.nextLine()
        try {
            return dateFormat.parse(input)
        } catch (e: Exception) {
            println("Entrada no válida. Use el formato 'yyyy-MM-dd'.")
        }
    }
}

// Eliminar una película
private fun eliminarPelicula(scanner: Scanner) {
    println("\n=== Eliminar Película ===")
    print("Ingrese el nombre de la película a eliminar: ")
    val nombre = scanner.nextLine()
    if (peliculas.removeIf { it.nombre.equals(nombre, ignoreCase = true) }) {
        println("Película eliminada exitosamente.")
    } else {
        println("Película no encontrada.")
    }
}

// Funciones para manejar actores
// Función para agregar un nuevo actor a una película existente
private fun agregarActor(scanner: Scanner) {
    println("\n=== Agregar Actor ===")
    val pelicula = seleccionarPelicula(scanner)
    if (pelicula != null) {
        println("Ingrese los datos del nuevo actor:")
        val actorNombre = solicitarCadena("Nombre: ", scanner)
        val edad = solicitarEntero("Edad: ", scanner)
        val nacionalidad = solicitarCadena("Nacionalidad: ", scanner)
        val ganadorOscar = solicitarBooleano("¿Ganador del Oscar? (true/false): ", scanner)
        val salario = solicitarDecimal("Salario: ", scanner)

        val nuevoActor = Actor(actorNombre, edad, nacionalidad, ganadorOscar, salario)
        pelicula.actores.add(nuevoActor)
        println("Actor añadido exitosamente a la película '${pelicula.nombre}'.")
    }
}

private fun leerActores(scanner: Scanner) {
    println("\n=== Leer Actores ===")
    val pelicula = seleccionarPelicula(scanner)
    if (pelicula != null) {
        if (pelicula.actores.isEmpty()) {
            println("No hay actores registrados para esta película.")
        } else {
            println("Actores de la película '${pelicula.nombre}':")
            pelicula.actores.forEachIndexed { index, actor ->
                println("Actor #${index + 1}: $actor")
            }
        }
    }
}

private fun actualizarActor(scanner: Scanner) {
    println("\n=== Actualizar Actor ===")
    val pelicula = seleccionarPelicula(scanner)
    if (pelicula != null) {
        if (pelicula.actores.isEmpty()) {
            println("No hay actores registrados para esta película.")
            return
        }

        println("Seleccione el actor a actualizar:")
        pelicula.actores.forEachIndexed { index, actor ->
            println("${index + 1}. $actor")
        }
        val indice = solicitarEntero("Número del actor: ", scanner) - 1

        if (indice in pelicula.actores.indices) {
            val actor = pelicula.actores[indice]
            println("Actualizando al actor: $actor")
            actor.nombre = solicitarCadena("Nuevo Nombre (actual: ${actor.nombre}): ", scanner)
            actor.edad = solicitarEntero("Nueva Edad (actual: ${actor.edad}): ", scanner)
            actor.nacionalidad = solicitarCadena("Nueva Nacionalidad (actual: ${actor.nacionalidad}): ", scanner)
            actor.ganadorOscar = solicitarBooleano("¿Ganador del Oscar? (actual: ${actor.ganadorOscar}): ", scanner)
            actor.salario = solicitarDecimal("Nuevo Salario (actual: ${actor.salario}): ", scanner)
            println("Actor actualizado exitosamente.")
        } else {
            println("Actor no encontrado.")
        }
    }
}

private fun eliminarActor(scanner: Scanner) {
    println("\n=== Eliminar Actor ===")
    val pelicula = seleccionarPelicula(scanner)
    if (pelicula != null) {
        if (pelicula.actores.isEmpty()) {
            println("No hay actores registrados para esta película.")
            return
        }

        println("Seleccione el actor a eliminar:")
        pelicula.actores.forEachIndexed { index, actor ->
            println("${index + 1}. $actor")
        }
        val indice = solicitarEntero("Número del actor: ", scanner) - 1

        if (indice in pelicula.actores.indices) {
            val actor = pelicula.actores.removeAt(indice)
            println("Actor eliminado exitosamente: $actor")
        } else {
            println("Actor no encontrado.")
        }
    }
}

// Función para seleccionar una película
private fun seleccionarPelicula(scanner: Scanner): Pelicula? {
    if (peliculas.isEmpty()) {
        println("No hay películas registradas.")
        return null
    }

    println("Seleccione una película:")
    peliculas.forEachIndexed { index, pelicula ->
        println("${index + 1}. ${pelicula.nombre}")
    }
    val indice = solicitarEntero("Número de la película: ", scanner) - 1

    return if (indice in peliculas.indices) {
        peliculas[indice]
    } else {
        println("Película no encontrada.")
        null
    }
}


// Guardar los datos en el archivo
private fun guardarDatos() {
    try {
        ObjectOutputStream(FileOutputStream(FILE_NAME)).use { oos ->
            oos.writeObject(peliculas)
        }
    } catch (e: IOException) {
        println("Error al guardar los datos: ${e.message}")
    }
}

// Cargar los datos desde el archivo
private fun cargarDatos() {
    try {
        ObjectInputStream(FileInputStream(FILE_NAME)).use { ois ->
            peliculas = ois.readObject() as MutableList<Pelicula>
        }
    } catch (e: IOException) {
        peliculas = mutableListOf()
    } catch (e: ClassNotFoundException) {
        peliculas = mutableListOf()
    }
}