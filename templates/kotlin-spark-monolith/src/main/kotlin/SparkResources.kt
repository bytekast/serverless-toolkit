import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.before
import spark.Spark.get
import java.util.*
import kotlin.collections.set


object SparkResources {

  fun defineResources() {

    val mapper = jacksonObjectMapper()

    before(Filter { _: Request, response: Response -> response.type("application/json") })

    get("/hello") { _: Request, res: Response ->
      val pong = HashMap<String, String>()
      pong["pong"] = "Hello, World!"
      res.status(200)
      mapper.writeValueAsString(pong)
    }

    get("/ping") { _: Request, res: Response ->
      val pong = HashMap<String, String>()
      pong["pong"] = "Ping!"
      res.status(200)
      mapper.writeValueAsString(pong)
    }
  }

  @Throws(Exception::class)
  @JvmStatic
  fun main(args: Array<String>) {
    SparkResources.defineResources()
  }
}