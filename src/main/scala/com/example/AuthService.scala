package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import com.example.endpoint.Authenticate

//http://kufli.blogspot.com/search/label/Spray.io
//https://github.com/eigengo/akka-patterns/tree/master/server/api/src/main/scala/org/eigengo/akkapatterns/api

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class AuthServiceActor extends Actor with AuthService with Tracking {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context


  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(
    trackRequestResponse(routes)
  )
}


// this trait defines our service behavior independently from the service actor
trait AuthService extends HttpService {


  /*
  implicit val rejectionHandler = RejectionHandler = {
    case
  }*/
  val routes: Route = Authenticate | Authenticate
}
