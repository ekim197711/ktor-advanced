package com.example

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.*

fun Routing.spaceship(){
    var myship: SpaceShip = SpaceShip()
    get("/spaceship") {
        call.respondText("The current spaceship is: $myship",
            ContentType.Text.Plain)
    }
    get("/spaceshipjson") {
        call.respond(myship)
    }
    post("/spaceship"){
        myship = call.receive<SpaceShip>()
        call.respondText("You have update the spaceship: $myship",
            ContentType.Text.Plain)
    }
}