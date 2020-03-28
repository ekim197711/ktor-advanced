package com.example

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery

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

    fun defaultExchangeAndQueue() : RabbitService{
        val newConnection = gimmeFactory().newConnection()
        val channel = newConnection.createChannel()

        channel.exchangeDeclare("mikeexchange", "direct", true)
        channel.queueDeclare("mikequeue", true, false, true, emptyMap())
        channel.queueBind("mikequeue", "mikeexchange", "mykey")
        channel.close()
        newConnection.close()
        return this
    }

    fun startListening(){
        val connection = gimmeFactory().newConnection()
        val channel = connection.createChannel()
        val deliverCallback = DeliverCallback{consumerTag: String?, message: Delivery? ->
            println("Consuming message!: ${String(message!!.body)}")
        }
        val cancelCallback = CancelCallback{consumerTag: String? -> println("Cancelled... $consumerTag") }

        channel.basicConsume("mikequeue",
            false,
            deliverCallback,
            cancelCallback)

    }


}