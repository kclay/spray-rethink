package com.example

/**
 * Created by IntelliJ IDEA.
 * User: Keyston
 * Date: 10/29/13
 * Time: 8:20 PM 
 */
package object endpoint {

  implicit def builderToRoute(builder: RouteBuilder) = builder.build
}
