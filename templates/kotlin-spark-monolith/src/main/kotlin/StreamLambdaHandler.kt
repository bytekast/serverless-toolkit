import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler

import spark.Spark

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class StreamLambdaHandler : RequestStreamHandler {

  companion object {
    private val handler: SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> = initProxyHandler()
    private fun initProxyHandler(): SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> {
      val proxyHandler = SparkLambdaContainerHandler.getAwsProxyHandler()
      SparkResources.defineResources()
      Spark.awaitInitialization()
      return proxyHandler
    }
  }

  @Throws(IOException::class)
  override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
    handler!!.proxyStream(inputStream, outputStream, context)
  }
}
