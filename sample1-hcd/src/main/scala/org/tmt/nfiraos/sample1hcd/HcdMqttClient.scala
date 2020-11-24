package org.tmt.nfiraos.sample1hcd

import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttCallback, MqttClient, MqttMessage}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class HcdMqttClient {

  val brokerUrl             = "tcp://localhost:1883"
  val REGISTER_DEVICE_TOPIC = "registerDevice"
  val SENSOR_READING_TOPIC  = "sensor/reading"

  val client = new MqttClient(brokerUrl, MqttClient.generateClientId, new MemoryPersistence)

  def setup() = {
    client.connect
    client.subscribe(REGISTER_DEVICE_TOPIC)
    client.subscribe(SENSOR_READING_TOPIC)

    setMqttCallback
  }

  private def setMqttCallback = {
    client.setCallback(new MqttCallback {

      override def messageArrived(topic: String, message: MqttMessage): Unit = {
        println("Receiving Data, Topic : %s, Message : %s".format(topic, message))

        topic match {
          case REGISTER_DEVICE_TOPIC => println(s"I received $REGISTER_DEVICE_TOPIC : " + message.toString)
          case SENSOR_READING_TOPIC  => println(s"I received $SENSOR_READING_TOPIC  : " + message.toString)
          case _                     =>
        }
      }

      override def connectionLost(cause: Throwable): Unit = println(cause)

      override def deliveryComplete(token: IMqttDeliveryToken): Unit = {}
    })
  }
}
