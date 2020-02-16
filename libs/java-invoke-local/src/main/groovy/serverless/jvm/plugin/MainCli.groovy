package serverless.jvm.plugin


import picocli.CommandLine

@CommandLine.Command(name = "java-invoke-local", version = "0.0.5",
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
    final request = new InvokeRequest(artifact?.toString(), handler, data, input?.toString(), function, jsonOutput, serverlessOffline)
    println request.run()
  }

  static void main(String[] args) {
    if ('--java-version' in args) {
      println System.getProperty("java.version")
    } else if ('--server' in args) {
      LocalServer.main(args)
    } else {
      System.exit(new CommandLine(new MainCli()).execute(args))
    }
  }
}
