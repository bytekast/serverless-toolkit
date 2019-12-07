package serverless

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.collection.mutable

class Handler extends RequestHandler[mutable.LinkedHashMap[String, Object], Response] {

  def handleRequest(input: mutable.LinkedHashMap[String, Object], context: Context): Response = {
    Response("Go Serverless v1.0! Your function executed successfully!", input)
  }
}
