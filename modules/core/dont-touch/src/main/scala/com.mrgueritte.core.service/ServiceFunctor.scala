package com.mrgueritte.core.service

import cats.effect.IO
import com.mrgueritte.core.client.Client
import com.mrgueritte.core.model.{Restaurant, Weather}
import com.mrgueritte.core.result.Result

class ServiceFunctor {

  def getWeatherByCitiesSync(cities: String*): Seq[Result[String, Weather]]=
    Client.getWeatherByCitiesSync(cities: _*).toList

  def getRestaurantAsync(city: String): IO[Result[String, List[Restaurant]]] =
    Client.getRestaurantAsync(city)

}
