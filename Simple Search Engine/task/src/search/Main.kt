package search

import java.io.File

/**
 * Search strategies
 *
 * @constructor Create new search strategy
 */
enum class SearchStrategy {
    ALL,
    ANY,
    NONE
}

/**
 * Mock data
 */
const val mockData = "Kristofer Galley\n" +
        "Fernando Marbury fernando_marbury@gmail.com\n" +
        "Kristyn Nix nix-kris@gmail.com\n" +
        "Regenia Enderle\n" +
        "Malena Gray\n" +
        "Colette Mattei\n" +
        "Wendolyn Mcphillips\n" +
        "Jim Gray\n" +
        "Coreen Beckham\n" +
        "Bob Yeh bobyeah@gmail.com\n" +
        "Shannan Bob stropeshah@gmail.com\n" +
        "Yer Fillion\n" +
        "Margene Resendez marres@gmail.com\n" +
        "Blossom Ambler\n" +
        "Teri Ledet teri_ledet@gmail.com\n" +
        "Dana Baron baron@gmail.com\n" +
        "Abram Goldsberry\n" +
        "Yer Leopold\n" +
        "Stefania Trunzo\n" +
        "Alexis Leopold\n" +
        "Carlene Bob\n" +
        "Oliver Dacruz\n" +
        "Jonie Richter\n" +
        "Pasquale Gallien gallien@evilcorp.com\n" +
        "Verdie Gentle\n" +
        "Gerardo Strouth gallien@evilcorp.com\n" +
        "Agripina Bob\n" +
        "Latricia Niebuhr\n" +
        "Malena Schommer\n" +
        "Drema Leopold\n" +
        "Heide Payeur\n" +
        "Ranae Digiovanni\n" +
        "Simona Pereira\n" +
        "Nick Digiovanni\n" +
        "Angelita Wigington gallien@evilcorp.com\n" +
        "Elin Gray\n" +
        "Dwain Trunzo\n" +
        "Boris Beiler\n" +
        "Remi Malek fsociefy@gmail.com\n" +
        "Demetria Hostetler gallien@evilcorp.com\n" +
        "Nydia Mcduffie\n" +
        "Florencio Defibaugh\n" +
        "Warner Giblin\n" +
        "Bob Mans\n" +
        "Shu Gray\n" +
        "Kaycee Gray\n" +
        "Victorina Froehlich victory@gmail.com\n" +
        "Roseanne Gray\n" +
        "Erica Radford hisam@gmail.com\n" +
        "Elyse Pauling"

/**
 * Command flow loop and entrypoint.
 *
 * @param args command line arguments
 */
fun main(args: Array<String>) {

    val entries = when {
        args.isNotEmpty() -> readEntriesFromFile(File(args[1]))
        else -> readEntriesFromString(mockData)
    }

    val index = createSearchIndex(entries)

    while (true) {

        when (readUserMenu()) {
            0 -> break
            1 -> findPerson(index, entries)
            2 -> printAllPersons(entries)
        }

    }

    println("Bye!")
}

/**
 * Read entries from file
 *
 * @param file input file
 * @return list of entries
 */
fun readEntriesFromFile(file: File): List<String> {

    var entries = emptyList<String>()

    if (file.exists()) {
        entries = file.readLines()
    }
    return entries
}

/**
 * Read entries from string
 *
 * @param data list of entries
 */
fun readEntriesFromString(data: String) = data.split("\n")

/**
 * Create search index for list of entries
 *
 * @param entries list of entries
 * @return map of word to entry
 */
fun createSearchIndex(entries: List<String>): Map<String, MutableList<Int>> {
    val index = mutableMapOf<String, MutableList<Int>>()

    entries.forEachIndexed { line_number, entry ->
        entry.lowercase().split(" ").forEach { word ->
            index.putIfAbsent(word, mutableListOf(line_number))?.also {
                index[word]?.add(line_number)
            }
        }
    }

    return index.toMap()
}

/**
 * Prints user menu and reads the user input
 *
 * @return user input
 */
fun readUserMenu(): Int {

    while (true) {
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")

        val option = readln().toInt()
        val isValid = option in 0..2
        if (isValid) return option

        println("Incorrect option! Try again.")
    }

}

/**
 * Find person in entries
 *
 * @param index search index
 * @param entries list of entries
 */
fun findPerson(index: Map<String, MutableList<Int>>, entries: List<String>) {

    println("Select a matching strategy: ALL, ANY, NONE")
    val strategy = SearchStrategy.valueOf(readln())

    println("Enter a name or email to search all matching people.")
    val queries = readln().lowercase().split(" ")
    val results = when (strategy) {
        SearchStrategy.ALL -> entries.filter { entry -> queries.map { it in entry.lowercase() }.all { it } }
        SearchStrategy.ANY -> index.filterKeys { it in queries }.values.flatten().map { entries[it] }
        SearchStrategy.NONE -> entries.filter { entry -> queries.map { it !in entry.lowercase() }.all { it } }
    }

    val numberResults = results.size

    println("$numberResults persons found:")
    results.forEach { println(it) }
}

/**
 * Prints all persons in the entries
 *
 * @param entries list of entries
 */
fun printAllPersons(entries: List<String>) {
    println("=== List of people ===")
    entries.forEach { println(it) }
}

