package search

import java.io.File

enum class SearchStrategy {
    ALL,
    ANY,
    NONE
}

fun main(args: Array<String>) {

    val file = File(args[1])
    val entries = readEntriesFromFile(file)
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

fun readEntriesFromFile(file: File): List<String> {

    var entries = emptyList<String>()

    if (file.exists()) {
        entries = file.readLines().toList()
    }
    return entries
}

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

fun printAllPersons(entries: List<String>) {
    println("=== List of people ===")
    entries.forEach { println(it) }
}

