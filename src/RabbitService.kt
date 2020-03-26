package com.example

import com.rabbitmq.client.ConnectionFactory

class RabbitService {
    private val connectionFactory: ConnectionFactory
    init {
        connectionFactory = ConnectionFactory()
        connectionFactory.host = "localhost"
        connectionFactory.port = 5672
        connectionFactory.virtualHost = "/"
        connectionFactory.username = "guest"
        connectionFactory.password = "guest"
    }

    fun gimmeFactory(): ConnectionFactory{
        return connectionFactory
    }

    fun defaultExchangeAndQueue(){
        val newConnection = gimmeFactory().newConnection()
        val channel = newConnection.createChannel()

        channel.exchangeDeclare("mikeexchange", "direct", true)
        channel.queueDeclare("mikequeue", true, false, true, emptyMap())
        channel.queueBind("mikequeue", "mikeexchange", "mykey")
        channel.close();
        newConnection.close();
    }


}