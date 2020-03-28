package com.example

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post


fun Routing.spaceship(){
    var myship: SpaceShip = SpaceShip()
    val connection = RabbitService().gimmeFactory().newConnection()
    val channel = connection.createChannel()
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

        channel.basicPublish("mikeexchange",
        "mykey", null, myship.toString().toByteArray())
    }
}