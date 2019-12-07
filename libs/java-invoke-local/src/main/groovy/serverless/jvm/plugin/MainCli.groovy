package serverless.jvm.plugin

import groovy.json.JsonOutput
import picocli.CommandLine

@CommandLine.Command(name = "java-invoke-local", version = "0.0.1",
  mixinStandardHelpOptions = true, // add --help and --version options
  description = "@|bold Invoke JVM Lambda Package|@ @|underline Locally|@")
class MainCli implements Runnable {

  @CommandLine.Option(names = ["-a", "--artifact"], description = "package artifact", required = true)
  File artifact

  @CommandLine.Option(names = ["-c", "--class"], description = "handler class", required = true)
  String handler

  @CommandLine.Option(names = ["-d", "--data"], description = "input data", required = false)
  String data

  @CommandLine.Option(names = ["-i", "--input-file"], description = "input file", required = false)
  File input

  @CommandLine.Option(names = ["-f", "--function"], description = "function name", required = false)
  String function

  @CommandLine.Option(names = ["--json-output"], description = "convert output to json", required = false)
  boolean jsonOutput

  @CommandLine.Option(names = ["--serverless-offline"], description = "called from serverless-offline", required = false)
  boolean serverlessOffline

  void run() {
    final classLoader = LambdaClassLoader.getClassLoader(artifact)
    final lambda = LambdaFunction.create(handler, classLoader)
    def result
    if (input) {
      result = LocalInvocation.invoke(lambda, input.text, function)
    } else {
      result = LocalInvocation.invoke(lambda, data, function)
    }

    if (jsonOutput) {
      try {
        if(serverlessOffline) {
          println JsonOutput.toJson(['__offline_payload__': result])
        } else {
          println JsonOutput.prettyPrint(JsonOutput.toJson(result))
        }
      } catch (e) {
        print result
      }
    } else {
      print result
    }
  }

  static void main(String[] args) {
    System.exit(new CommandLine(new MainCli()).execute(args))
  }
}
