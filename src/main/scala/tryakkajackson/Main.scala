/*
 * Copyright 2020 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tryakkajackson

import akka.actor.ActorSystem
import akka.serialization.{ Serialization, SerializationExtension }
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Main {

  def main(args: Array[String]): Unit = {
    val system        = ActorSystem()
    val serialization = SerializationExtension(system)

    useAkkaSerializer(cbor.Foo("bar", 42), serialization)
    useAkkaSerializer(json.Foo("bar", 42), serialization)

    system.terminate()
    println()

    useJacksonDirectly(cbor.Foo("bar", 42), Some(new CBORFactory()))
    useJacksonDirectly(json.Foo("bar", 42))
  }

  private def useAkkaSerializer(o: AnyRef, serialization: Serialization): Unit = {
    val serializer  = serialization.findSerializerFor(o)
    val id          = serializer.identifier
    val bytes       = serializer.toBinary(o)
    val bytesString = bytes.map(b => f"$b%h").mkString
    println(s"$id: $bytesString")
  }

  private def useJacksonDirectly(o: AnyRef, factory: Option[JsonFactory] = None) = {

    // This is how Akka is using the Jackson API which results in always using JSON!
    //val builder = factory.map(new JsonFactoryBuilder(_)).getOrElse(new JsonFactoryBuilder())
    //val mapper = new ObjectMapper(builder.build())

    // This works as expected!
    val mapper = factory.map(new ObjectMapper(_)).getOrElse(new ObjectMapper())

    mapper.registerModule(DefaultScalaModule)
    val bytes       = mapper.writeValueAsBytes(o)
    val bytesString = bytes.map(b => f"$b%h").mkString
    println(s"$factory: $bytesString")
  }
}
