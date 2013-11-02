package com.example.endpoint

import spray.routing.{RouteConcatenation, RequestContext, Route, Directives}
import com.example.authentication.{NonceValidator, BasicSignedRequestAuthenticator}

import spray.routing._
import spray.http.FormData
import shapeless.HNil

/**
 * Created by IntelliJ IDEA.
 * User: Keyston
 * Date: 10/25/13
 * Time: 4:34 PM 
 */

class RouteBuilder(routes: Seq[Route]) extends RouteConcatenation {


  def build: Route = routes.reduce(_ ~ _)

  def |(e: Endpoint[_]) = new RouteBuilder(routes :+ e.route)

}

trait Endpoint[T] extends Directives {


  def |(e: Endpoint[_]) = new RouteBuilder(Seq(route, e.route))

  private[this] val authenticator = new BasicSignedRequestAuthenticator(NonceValidator)

  implicit def auth(f: RequestContext => Route) = authorize(authenticator)

  def allFormFields: Directive[Map[String, String] :: HNil] = {
    extract(_.request.entity.as[FormData].right.map(_.fields)).flatMap {
      case Right(value) => provide(value)
      case Left(_) => reject
    }
  }

  private[this] def base(endpoint: String) = authorize(authenticator) & path(endpoint)


  def apiPost(endpoint: String)(f: RequestContext => Unit) = (base(endpoint) & post) {
    ctx => f(ctx)
  }


  val route: Route

}