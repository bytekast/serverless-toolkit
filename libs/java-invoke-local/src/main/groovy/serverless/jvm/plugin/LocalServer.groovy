package serverless.jvm.plugin

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import groovy.json.JsonSlurper

class LocalServer {

  private static JsonSlurper slurper = new JsonSlurper()

  static void main(String[] args) {
    startServer()
  }

  static startServer() {
    HttpServer.create(new InetSocketAddress(8080), 0).with {
      createContext("/invoke") { http ->
        if (http.requestMethod.toLowerCase() != 'post') {
          http.sendResponseHeaders(405, 0)
          http.responseBody.withWriter { out -> }
        } else {
          http.responseHeaders.add("Content-type", "text/plain")
          http.sendResponseHeaders(200, 0)
          http.responseBody.withWriter { out ->
            out << handleRequest(http)
          }
        }
      }
      start()
    }
  }

  static String handleRequest(HttpExchange http) {
    try {
      final body = http.requestBody?.text
      if (body) {
        final payload = parsePayload(body)
        return new InvokeRequest(payload).run()
      } else {
        return body
      }
    } catch (e) {
      e.printStackTrace()
      return e.message
    }
  }

  static Map parsePayload(String payload) {
    try {
      return slurper.parseText(payload)
    } catch (e) {
      throw new Exception('Unknown payload')
    }
  }
}
