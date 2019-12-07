package serverless.jvm.plugin.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DoubleLongRequestHandler implements RequestHandler<Double, Long> {
  @Override
  public Long handleRequest(Double input, Context context) {
    return input.longValue();
  }

}
