package serverless

import scala.beans.BeanProperty
import scala.collection.mutable

case class Response(@BeanProperty message: String, @BeanProperty request: mutable.LinkedHashMap[String, Object])
