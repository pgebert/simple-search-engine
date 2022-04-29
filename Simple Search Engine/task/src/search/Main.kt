package search

import java.io.File

enum class SearchStrategy {
    ALL,
    ANY,
    NONE
}

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

fun readEntriesFromFile(file: File): List<String> {

    var entries = emptyList<String>()

    if (file.exists()) {
        entries = file.readLines()
    }
    return entries
}

fun readEntriesFromString(data: String) = data.split("\n")


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

