import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import groovy.transform.CompileStatic
import spark.Spark

@CompileStatic
class StreamLambdaHandler implements RequestStreamHandler {
  private static SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler
  static {
    try {
      handler = SparkLambdaContainerHandler.getAwsProxyHandler()
      SparkResources.defineResources()
      Spark.awaitInitialization()
    } catch (ContainerInitializationException e) {
      // if we fail here. We re-throw the exception to force another cold start
      e.printStackTrace()
      throw new RuntimeException("Could not initialize Spark container", e)
    }
  }

  @Override
  void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
    throws IOException {
    handler.proxyStream(inputStream, outputStream, context)
  }
}
