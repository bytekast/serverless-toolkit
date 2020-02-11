@Grab('com.amazonaws.serverless:aws-serverless-java-container-spark:1.4')
@Grab('com.sparkjava:spark-core:2.9.1')
@Grab('com.fasterxml.jackson.core:jackson-databind:2.10.1')

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import groovy.transform.CompileStatic
import spark.Spark
import Routes

@CompileStatic
class Handler implements RequestStreamHandler {
  private static SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler
  static {
    try {
      handler = SparkLambdaContainerHandler.getAwsProxyHandler()
      Routes.main()
      Spark.awaitInitialization()
    } catch (ContainerInitializationException e) {
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



