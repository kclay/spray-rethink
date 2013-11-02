package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import spray.client.pipelining._

class AuthServiceSpec extends Specification with Specs2RouteTest with AuthService {
  def actorRefFactory = system


  def sign = ???
  "MyService" should {

      "return hello from GET request on root path" in {
        Get() ~>
      }
    /*  "return a greeting for GET requests to the root path" in {
        Get() ~> routes ~> check {
          responseAs[String] must contain("Say hello")
        }
      }

      "leave GET requests to other paths unhandled" in {
        Get("/kermit") ~> routes ~> check {
          handled must beFalse
        }
      }

      "return a MethodNotAllowed error for PUT requests to the root path" in {
        Put() ~> sealRoute(routes) ~> check {
          status === MethodNotAllowed
          responseAs[String] === "HTTP method not allowed, supported methods: GET"
        }
      } */
  }
}
