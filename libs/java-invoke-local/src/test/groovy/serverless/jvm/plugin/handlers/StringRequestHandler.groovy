package serverless.jvm.plugin.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class StringRequestHandler implements RequestHandler<String, String> {
  @Override
  String handleRequest(String input, Context context) {
    return input
  }
}
