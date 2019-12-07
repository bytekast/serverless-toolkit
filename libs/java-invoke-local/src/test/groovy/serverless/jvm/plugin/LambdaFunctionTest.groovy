package serverless.jvm.plugin

import spock.lang.Specification

class LambdaFunctionTest extends Specification {

  def "should fail with missing handler method"() {
    given:
    def handler = 'com.serverless.Response'
    def zipFile = this.class.getResource("/hello.zip").path

    when:
    def classLoader = LambdaClassLoader.getClassLoader(zipFile)
    def function = LambdaFunction.create(handler, classLoader)

    then:
    Exception ex = thrown()
    ex.message == 'Unable to detect handler method name'
  }


  def "should fail with handler method not found"() {
    given:
    def handler = 'com.serverless.Handler:missingMethod'
    def zipFile = this.class.getResource("/hello.zip").path

    when:
    def classLoader = LambdaClassLoader.getClassLoader(zipFile)
    def function = LambdaFunction.create(handler, classLoader)

    then:
    Exception ex = thrown()
    ex.message == 'Method name does not exist'
  }

  def "should detect lambda function class and method in zip file"() {
    given:
    def handler = 'com.serverless.Handler::handleRequest'
    def zipFile = new File(this.class.getResource("/hello.zip").path)

    when:
    def classLoader = LambdaClassLoader.getClassLoader(zipFile)
    def function = LambdaFunction.create(handler, classLoader)

    then:
    noExceptionThrown()
    function.method == 'handleRequest'
    function.instance != null
    function.returnType == Class.forName('com.serverless.ApiGatewayResponse', true, classLoader)
    function.parameterType == Map
  }

  def "should detect lambda function class and method in uber jar"() {
    given:
    def handler = 'com.serverless.Handler::handleRequest'
    def jarFile = new File(this.class.getResource("/jtest.jar").path)

    when:
    def classLoader = LambdaClassLoader.getClassLoader(jarFile)
    def function = LambdaFunction.create(handler, classLoader)

    then:
    noExceptionThrown()
    function.method == 'handleRequest'
    function.instance != null
    function.returnType == Class.forName('com.serverless.ApiGatewayResponse', true, classLoader)
    function.parameterType == Map
  }

  def "should detect lambda function class and method in uber jar without method specified in handler"() {
    given:
    def handler = 'com.serverless.Handler'
    def jarFile = new File(this.class.getResource("/jtest.jar").path)

    when:
    def classLoader = LambdaClassLoader.getClassLoader(jarFile)
    def function = LambdaFunction.create(handler, classLoader)

    then:
    noExceptionThrown()
    function.method == 'handleRequest'
    function.instance != null
    function.returnType == Class.forName('com.serverless.ApiGatewayResponse', true, classLoader)
    function.parameterType == Map
  }
}
