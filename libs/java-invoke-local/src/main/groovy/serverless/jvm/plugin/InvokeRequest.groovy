package serverless.jvm.plugin

import groovy.json.JsonOutput
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.Memoized

@Canonical
class InvokeRequest {
  String artifact
  String handler
  String data
  String inputFile
  String function
  boolean jsonOutput
  boolean serverlessOffline

  @CompileStatic
  def run() {

    final artifactFile = new File(artifact)
    if (!artifactFile.exists()) {
      throw new Exception("Unable to find artifact: ${artifact}")
    }
    final lambda = load(artifactFile.lastModified(), artifactFile, handler)

    def result
    File input = inputFile ? new File(inputFile) : null
    if (input) {
      result = LocalInvocation.invoke(lambda, input.text, function)
    } else {
      result = LocalInvocation.invoke(lambda, data, function)
    }

    if (jsonOutput) {
      try {
        if (serverlessOffline) {
          return JsonOutput.toJson(['__offline_payload__': result])
        } else {
          return JsonOutput.prettyPrint(JsonOutput.toJson(result))
        }
      } catch (e) {
        return result
      }
    } else {
      return result
    }
  }

  @CompileStatic
  @Memoized
  static LambdaFunction load(long lastModified, File artifact, String handler) {
    final classLoader = LambdaClassLoader.getClassLoader(artifact)
    final lambda = LambdaFunction.create(handler, classLoader)
    return lambda
  }
}
