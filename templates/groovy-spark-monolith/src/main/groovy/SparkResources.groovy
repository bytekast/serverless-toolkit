import groovy.json.JsonOutput
import spark.Filter

import static spark.Spark.before
import static spark.Spark.get

class SparkResources {

  static void defineResources() {

    before({ request, response -> response.type("application/json") } as Filter)

    get("/ping") { req, res ->
      res.status(200)
      JsonOutput.toJson(["pong": "Hello World!"])
    }
  }

  static void main(String[] args) throws Exception {
    SparkResources.defineResources()
  }
}