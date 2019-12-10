package serverless.jvm.plugin

import groovy.json.JsonOutput
import serverless.jvm.plugin.handlers.*
import serverless.jvm.plugin.models.Address
import serverless.jvm.plugin.models.Coordinates
import spock.lang.Specification

class LocalInvocationTest extends Specification {

  def 'test IntRequestHandler'() {
    given:
    def lambda = LambdaFunction.create(IntRequestHandler.name, this.class.classLoader)

    when:
    def result = LocalInvocation.invoke(lambda, "500")

    then:
    result instanceof Integer
    result == 500
  }

  def 'test DoubleLongRequestHandler'() {
    given:
    def lambda = LambdaFunction.create(DoubleLongRequestHandler.name, this.class.classLoader)

    when:
    def result = LocalInvocation.invoke(lambda, "500.7654321")

    then:
    result instanceof Long
    result == 500
  }

  def 'test StringRequestHandler'() {
    given:
    def lambda = LambdaFunction.create(StringRequestHandler.name, this.class.classLoader)

    when:
    def result = LocalInvocation.invoke(lambda, 'test')

    then:
    result instanceof String
    result == 'test'
  }

  def 'test SimpleRequestStreamHandler'() {
    given:
    def lambda = LambdaFunction.create(SimpleRequestStreamHandler.name, this.class.classLoader)

    when:
    def result = LocalInvocation.invoke(lambda, 'test')

    then:
    result instanceof ByteArrayOutputStream
    result.toString() == 'test'
  }

  def 'test PojoRequestHandler'() {
    given:
    def lambda = LambdaFunction.create(PojoRequestHandler.name, this.class.classLoader)
    def address = new Address("road", "city", "state", "zip")

    when:
    def result = LocalInvocation.invoke(lambda, JsonOutput.toJson(address))

    then:
    result instanceof Coordinates
    result.lon == 0.0
    result.lat == 0.0
  }

  def 'test PojoRequestHandler failure'() {
    given:
    def lambda = LambdaFunction.create(PojoRequestHandler.name, this.class.classLoader)

    when:
    def result = LocalInvocation.invoke(lambda, "test")

    then:
    Exception e = thrown()
    e.message == 'Unable to convert input "test" to serverless.jvm.plugin.models.Address'
  }
}
