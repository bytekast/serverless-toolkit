package serverless

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class HandlerSuite extends FunSuite {
  test("handleRequest returns") {
    def handler = new Handler()

    assert(handler.handleRequest(mutable.LinkedHashMap("foo" -> "bar"), null).message ==
      "Go Serverless v1.0! Your function executed successfully!")
  }
}
