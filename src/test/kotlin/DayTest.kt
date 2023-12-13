import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

open class DayTest {

    protected fun solvePart1(day: Day): Long {
        val start = System.currentTimeMillis()
        val part1 = day.part1()
        println("Part 1: $part1 in ${System.currentTimeMillis() - start}ms")
        return part1
    }

    protected fun solvePart2(day: Day): Long {
        val start = System.currentTimeMillis()
        val part2 = day.part2()
        println("Part 2: $part2 in ${System.currentTimeMillis() - start}ms")
        return part2
    }

    protected fun String.readInput(): String {
        val path = "src/test/kotlin/day${this}/input"
        if (!File(path).exists()) {
            downloadFileIfNotExists("https://adventofcode.com/2023/day/${this}/input", path)
        }
        return File(path).readText().trim()
    }


    private fun downloadFileIfNotExists(url: String, localPath: String) {
        val file = File(localPath)
        if (!file.exists()) {
            val sessionCookie = File("src/test/kotlin/sessionCookie.txt").readText().trim()
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("Cookie", "session=$sessionCookie")
            val input: InputStream = connection.inputStream
            Files.copy(input, Paths.get(localPath), StandardCopyOption.REPLACE_EXISTING)
            println("File downloaded and saved to $localPath")
        } else {
            println("File already exists at $localPath")
        }
    }

    protected fun String.readTestInput(): String {
        return File("src/test/kotlin/day${this}/testInput").readText().trim()
    }
}