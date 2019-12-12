package serverless.jvm.plugin.api

import com.amazonaws.services.lambda.runtime.RequestHandler

class Route {
  String method
  String path
  RequestHandler<String, String> handler
}
