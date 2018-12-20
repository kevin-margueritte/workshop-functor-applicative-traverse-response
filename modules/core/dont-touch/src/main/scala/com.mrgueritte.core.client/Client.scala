package com.mrgueritte.core.client

import cats.effect.IO
import com.mrgueritte.core.database.{CityDAO, RestaurantDAO}
import com.mrgueritte.core.model.{Behaviour, Restaurant, Weather}
import com.mrgueritte.core.result.{Error, Ok, Result}

object Client {


  def getWeatherAsync(city: String): IO[Result[String, Weather]] = {
    CityDAO.getCityByName(city).flatMap {
      case None       => IO.pure(Error(s"Unknown city : $city"))
      case Some(city) if city.name == "Montpellier" => IO.pure(Ok(Weather(Behaviour.Sunshine, city)))
      case Some(city) => IO.pure(Ok(Weather(Behaviour.Rainy, city)))
    }
  }

  def getWeatherSync(city: String): Result[String, Weather] =
    getWeatherAsync(city).unsafeRunSync()

  def getWeatherByCitiesSync(cities: String*): Seq[Result[String, Weather]] = {
    cities.map(Client.getWeatherSync)
  }

  def getRestaurantAsync(city: String): IO[Result[String, List[Restaurant]]] = {
    IO {
      RestaurantDAO.getByCityNameSync(city) match {
        case Nil => Error(s"No restaurant : $city")
        case values => Ok(values.toList)
      }
    }
  }

  def getRestaurantSync(city: String): Result[String, List[Restaurant]] =
    getRestaurantAsync(city).unsafeRunSync()

}
