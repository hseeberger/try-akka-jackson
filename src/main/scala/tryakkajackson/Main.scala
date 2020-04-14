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

object Main {

  def main(args: Array[String]): Unit = {
    val system        = ActorSystem()
    val serialization = SerializationExtension(system)
    run(cbor.Foo("bar", 42), serialization)
    run(json.Foo("bar", 42), serialization)

    system.terminate()
  }

  private def run(o: AnyRef, serialization: Serialization): Unit = {
    val serializer  = serialization.findSerializerFor(o)
    val id          = serializer.identifier
    val bytes       = serializer.toBinary(o)
    val bytesString = bytes.map(b => f"$b%h").mkString
    println(s"$id: $bytesString")
  }
}
