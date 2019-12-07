package serverless.jvm.plugin.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import serverless.jvm.plugin.models.Address
import serverless.jvm.plugin.models.Coordinates

class PojoRequestHandler implements RequestHandler<Address, Coordinates> {
  @Override
  Coordinates handleRequest(Address input, Context context) {
    return new Coordinates(0.0, 0.0)
  }
}
