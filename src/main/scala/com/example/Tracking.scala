package com.example

import spray.http._
import spray.routing._
import scala.concurrent.Future
import spray.routing.directives.BasicDirectives

/**
 * Created by IntelliJ IDEA.
 * User: Keyston
 * Date: 10/25/13
 * Time: 3:58 PM 
 */
trait Tracking {
  this: BasicDirectives with HttpService =>


  def trackRequestT(request: HttpRequest): Any => Unit = {
    /* val path = request.uri.split('?')(0) // not ideal for parameters in the path, e.g. uuids.
     val ip = request.headers.find(_.name == "Remote-Address").map { _.value }
     val auth = getToken(request)
     val stat = TrackingStat(path, ip, auth, "request")

     // the HttpService dispatcher is used to execute these inserts
     Future{trackingMongo.insertFast(stat)}     */

    // the code is executed when called, so the date is calculated when the response is ready
    (r: Any) => (println(r))
  }

  def trackRequestResponse: Directive0 = mapRequestContext {
    ctx =>
      val logResponse = trackRequestT(ctx.request)
      ctx.withRouteResponseMapped {
        response => logResponse(response);
          response


      }

  }


}