package com.example.endpoint


/**
 * Created by IntelliJ IDEA.
 * User: Keyston
 * Date: 10/25/13
 * Time: 4:36 PM 
 */

case class AuthenticateRequest(username: String, password: String, authType: String)


object Authenticate extends Authenticate

class Authenticate extends Endpoint[AuthenticateRequest] {

  val params = parameters('username.as[String], 'password.as[String], 'auth_type.as[String]).as(AuthenticateRequest)

  val route = apiPost("authenticate") {
    ctx => ctx.complete("Hello")
  }

}
