package com.mrgueritte.core.service

import cats.effect.IO
import com.mrgueritte.core.client.Client
import com.mrgueritte.core.model.{Behaviour, Restaurant, Weather}
import com.mrgueritte.core.result.{Error, Ok, Result}

class ServiceApplicative {

  def getWeatherAsync(city: String): IO[Result[String, Weather]] =
    Client.getWeatherAsync(city)

  def getRestaurantSync(city: String): Result[String, List[Restaurant]] =
    Client.getRestaurantSync(city)

  def getRestaurantAsync(city: String): IO[Result[String, List[Restaurant]]] =
    Client.getRestaurantAsync(city)

  def isRain(city: String): Result[String, Weather] = {
    Client.getWeatherSync(city)match {
      case Ok(weather) if (Behaviour.Rainy != weather.behaviour) => Ok(weather)
      case Ok(weather) => Error("it's rain")
      case Error(err) => Error(err)
    }
  }

  def isRainAsync(city: String): IO[Result[String, Weather]] = {
    Client.getWeatherAsync(city).map {
      case Ok(weather) if (Behaviour.Rainy != weather.behaviour) => Ok(weather)
      case Ok(weather) => Error("it's rain")
      case Error(err) => Error(err)
    }
  }

  def prettyRestaurant(restaurants: Seq[Restaurant], weather: Weather): String =
    s"Hello, in ${weather.city.name} the weather is ${weather.behaviour} and restaurants open are [${restaurants.map(_.name).mkString(", ")}]"
}
