import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*

// Функция для проверки доступности веб-сайта
fun checkWebsite(url: String): Boolean {
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    }
}

// Список веб-сайтов для проверки
val websites = listOf(
    "https://www.google.com",
    "https://www.facebook.com",
    "https://www.github.com",
    "https://www.twitter.com",
    "https://www.instagram.com",
    "https://www.wikipedia.org",
    "https://stackoverflow.com/",
    "https://www.netflix.com",
    "https://music.yandex.ru",
    "https://habr.com"
)

// Главная функция
fun main() = runBlocking {
    // Создаем список задач для асинхронной проверки веб-сайтов
    val tasks = websites.map { url ->
        async {
            // Проверяем доступность веб-сайта и возвращаем пару (url, result)
            val result = checkWebsite(url)
            Pair(url, result)
        }
    }
    // Ожидаем завершения всех задач и выводим результаты на экран
    tasks.forEach { task ->
        val (url, result) = task.await()
        val status = if (result) "доступен" else "недоступен"
        println("Сайт $url $status")
    }
}