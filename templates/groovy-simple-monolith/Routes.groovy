@Grab('com.sparkjava:spark-core:2.9.1')
@Grab('org.codehaus.groovy:groovy-json:3.0.0')

import groovy.json.JsonOutput
import spark.Filter
import static spark.Spark.before
import static spark.Spark.get
import static spark.Spark.post

before({ request, response -> response.type("application/json") } as Filter)

get("/hello", (request, response) -> "Hello World!")

post("/hello", (request, response) -> "Hello World: " + request.body())

get("/private", (request, response) -> {
  response.status(401)
  return "Go Away!!!"
})

get("/users/:name", (request, response) -> "Selected user: " + request.params(":name"))

get("/news/:section", (request, response) -> {
  response.type("text/xml")
  return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>"
})

get("/protected", (request, response) -> {
  halt(403, "I don't think so!!!")
  return null
})

get("/redirect", (request, response) -> {
  response.redirect("/news/world")
  return null
})
