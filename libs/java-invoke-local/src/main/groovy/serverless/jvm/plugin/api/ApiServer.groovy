package serverless.jvm.plugin.api

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Response

class ApiServer {

  static final ObjectMapper mapper = new ObjectMapper()

  static void start(List<Route> routes = []) {

    final handlers = routes.collect { route ->
      [
        method : route.method,
        path   : route.path,
        handler: { req, res ->
          try {
            final apiReq = convertRequest(req)
            final stringResponse = route.handler.handleRequest(mapper.writeValueAsString(apiReq), null as Context)
            final apiRes = mapper.readValue(stringResponse, APIGatewayProxyResponseEvent)
            convertResponse(apiRes, res)
          } catch (e) {
            e.message
          }
        }
      ]
    }

    handlers.each { route ->
      spark.Spark."${route.method.toLowerCase()}"("${route.path}", route.handler)
    }
  }

  static APIGatewayProxyRequestEvent convertRequest(Request request) {
    final event = new APIGatewayProxyRequestEvent()
    event.body = request.body()
    event.headers = request.headers().collectEntries { [it, request.headers(it)] }
    event.httpMethod = request.requestMethod()?.toUpperCase()
    event.pathParameters = request.params()
    event.path = request.contextPath()

    event.queryStringParameters = [:]
    event.multiValueQueryStringParameters = [:]
    request.queryMap().toMap().each { k, v ->
      if (v.length > 1) {
        event.multiValueQueryStringParameters.put(k, v)
      } else {
        event.queryStringParameters.put(k, v?.first())
      }
    }
    event
  }

  static String convertResponse(APIGatewayProxyResponseEvent apiResponse, Response httpResponse) {
    httpResponse.status(apiResponse.statusCode)
    apiResponse.headers?.each { k, v ->
      httpResponse.header(k, v)
    }
    apiResponse.body
  }
}
