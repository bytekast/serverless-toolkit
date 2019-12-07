package serverless.jvm.plugin.models

import groovy.transform.Canonical

@Canonical
class Address {
  String street
  String city
  String state
  String zipCode
}
