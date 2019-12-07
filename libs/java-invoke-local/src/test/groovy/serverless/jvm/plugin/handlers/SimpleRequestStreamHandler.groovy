package serverless.jvm.plugin.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler

class SimpleRequestStreamHandler implements RequestStreamHandler {
  @Override
  void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    output.write(input.getBytes())
  }
}
