import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlin.random.Random

suspend fun main() = coroutineScope {

    /**
     * readers for getAmount(id)
     */
    var rCount = 0

    /**
     * readers for addAmount(id,value)
     */
    var wCount = 0

    /**
     * available values for "id" and "value"
     */
    val idList: MutableSet<Int> = mutableSetOf()

    /**
     * read config
     */
    val file = File("config")
    try {
        BufferedReader(FileReader(file)).use { br ->
            rCount = br.readLine().toInt()
            wCount = br.readLine().toInt()
            val values = br.readLine().split(" ")
            values.forEach {
                idList.add(it.toInt())
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }

    /**
     * getAmount client job
     */
    val reader = launch{
        for(i in 1..rCount){
            val id = idList.random()
            println("r $i")
            client.get("http://localhost:8080/account/$id")
        }
    }

    /**
     * addAmount client job
     */
    val writer = launch{
        for(i in 1..wCount){
            val id = idList.random()
            println("w $i")
            client.post("http://localhost:8080/account") {
                contentType(ContentType.Application.Json)
                setBody(AddAmountRs(id, Random(1000).nextLong()))
            }
        }
    }

    reader.join()
    writer.join()
    client.close()
}