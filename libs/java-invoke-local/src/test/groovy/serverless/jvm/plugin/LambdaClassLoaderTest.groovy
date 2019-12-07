package serverless.jvm.plugin

import spock.lang.Specification

class LambdaClassLoaderTest extends Specification {

  def "should fail with unsupported file format"() {

    when:
    LambdaClassLoader.getClassLoader("test.tar")

    then:
    Exception ex = thrown()
    ex.message == 'Unsupported file format'
  }

  def "should fail with missing file"() {

    when:
    LambdaClassLoader.getClassLoader("test.jar")

    then:
    Exception ex = thrown()
    ex.message == 'test.jar does not exist'
  }
}
