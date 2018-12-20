package com.mrgueritte.exercice.traverse

import com.mrgueritte.core.model.Weather
import com.mrgueritte.core.result.Result
import com.mrgueritte.core.service.ServiceTraverse

object Process extends App {
  import cats.implicits._
  import com.mrgueritte.core.result.applicativeInstance._

  val service = new ServiceTraverse

  def getRestaurantsWithSequence(cities: String*): Result[String, List[Weather]] = {
    val result: List[Result[String, Weather]] = cities.map(service.getWeatherSync).toList
    result.sequence[Result[String, ?], Weather]
  }

  def getRestaurantsWithTraverse(cities: String*): Result[String, List[Weather]] = {
    cities.toList.traverse[Result[String, ?], Weather] { city =>
      service.getWeatherSync(city)
    }
  }

  def getRestaurantsWithTraverseListZ(cities: ListZ[String]): Result[String, ListZ[Weather]] = {
    cities.traverse[Result[String, ?], Weather] { city =>
      service.getWeatherSync(city)
    }
  }

  println("-- Weather with sequence --")
  println(getRestaurantsWithSequence("Paris", "Toulouse", "Montpellier"))
  println(" - with an error - ")
  println(getRestaurantsWithSequence("Paris", "Toulouse", "Barcelone", "Montpellier"))
  println(" ")
  println("-- Weather with traverse --")
  println(getRestaurantsWithTraverse("Paris", "Toulouse", "Montpellier"))
  println(" - with an error - ")
  println(getRestaurantsWithTraverse("Paris", "Toulouse", "Barcelone", "Montpellier"))
  println(" ")
  println("-- Weather with ListZ --")
  println(getRestaurantsWithTraverseListZ(ListZ("Paris", "Toulouse", "Montpellier")))
  println(" - with an error - ")
  println(getRestaurantsWithTraverseListZ(ListZ("Paris", "Toulouse", "Barcelone", "Montpellier")))

}

