import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
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

    val logger = LoggerFactory.getLogger(javaClass)

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
        logger.error(e.message)
    }

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }

    /**
     * addAmount client job
     */
    val writer = launch{
        for(i in 1..wCount){
            val id = idList.random()
            logger.info("addAmount($id)")
            client.post("http://localhost:8080/account") {
                contentType(ContentType.Application.Json)
                setBody(AddAmountRs(id, Random(1000).nextLong()))
            }
        }
    }

    /**
     * getAmount client job
     */
    val reader = launch{
        for(i in 1..rCount){
            val id = idList.random()
            logger.info("getAmount($id)")
            client.get("http://localhost:8080/account/$id")
        }
    }

    writer.join()
    reader.join()
    client.close()
}