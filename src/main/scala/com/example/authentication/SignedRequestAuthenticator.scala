package com.example.authentication


import spray.http._
import spray.routing.RequestContext
import spray.util._

/**
 * Created by IntelliJ IDEA.
 * User: Keyston
 * Date: 10/25/13
 * Time: 4:50 PM 
 */

sealed abstract class ModeledCompanion extends Renderable {
  val name = {
    val n = getClass.getName
    n.substring(n.indexOf('$') + 1, n.length - 1).replace("$minus", "-")
  }
  val lowercaseName = name.toLowerCase
  private[this] val nameBytes = name.getAsciiBytes

  def render[R <: Rendering](r: R): r.type = r ~~ nameBytes ~~ ':' ~~ ' '
}

sealed abstract class ModeledHeader extends HttpHeader with Serializable {
  def name: String = companion.name

  def value: String = renderValue(new StringRendering).get

  def lowercaseName: String = companion.lowercaseName

  def render[R <: Rendering](r: R): r.type = renderValue(r ~~ companion)

  def renderValue[R <: Rendering](r: R): r.type

  protected def companion: ModeledCompanion
}

object AuthToken extends ModeledCompanion {
  override val name = "X-AuthToken"

}


case class AuthToken(token: String) extends ModeledHeader {

  def renderValue[R <: Rendering](r: R): r.type = r ~~ token

  protected def companion = AuthToken

}


trait SignedRequestAuthenticator[U] extends PartialFunction[RequestContext, Boolean] {


  private[this] val AuthTokenHeader = "X-AuthToken"


  def authenticate(token: Option[String], ctx: RequestContext): Boolean

  def apply(ctx: RequestContext): Boolean = {


    val lowerCaseName = AuthTokenHeader.toLowerCase
    val authHeader = ctx.request.headers.mapFind {
      case HttpHeader(`lowerCaseName`, value) => Some(value)
      case _ => None
    }

    authenticate(authHeader, ctx)


  }

  def isDefinedAt(x: RequestContext) = true
}


trait Authenticated

object Authenticated extends Authenticated

trait AuthValidator {
  def validate(token: String, ctx: RequestContext): Boolean = sign(ctx.request.uri) == token

  def sign(uri: Uri): String
}

object NonceValidator extends AuthValidator {

  val signer = java.security.MessageDigest.getInstance("SHA-256")

  def sign(uri: Uri) = {

    val challenge = uri.path.toString + uri.query.toSeq.sorted.foldLeft("?") {
      case (s, (k, v)) => (if (s == "?") "" else "&") + s"$k=$v"
    }
    signer.digest(challenge.getBytes).foldLeft("")((s: String, b: Byte) => s +
      Character.forDigit((b & 0xf0) >> 4, 16) +
      Character.forDigit(b & 0x0f, 16))
  }


}

class BasicSignedRequestAuthenticator(val validator: AuthValidator) extends SignedRequestAuthenticator[Authenticated] {
  def authenticate(token: Option[String], ctx: RequestContext) = token.map(t => validator.validate(t, ctx)).getOrElse(false)


}