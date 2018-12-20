package com.mrgueritte.exercice.functor

import cats.Functor
import cats.effect.IO
import com.mrgueritte.core.model.{Behaviour, City, Restaurant, Weather}
import com.mrgueritte.core.result.Result
import com.mrgueritte.core.service.ServiceFunctor

object Process extends App {
  import cats.instances.list._
  import io.circe.syntax._
  import com.mrgueritte.core.result.functorInstance._
  import WeatherEncoder._

  val service = new ServiceFunctor

  /**
    * L'objectif est de retourner dans une liste un `Result` contenant `UnfoldUmbrella` s'il pleut sinon retourner `TakeFoldUmbrella`
    *
    * Règles :
    *   - utiliser la méthode `getWeatherByCitiesSync` du service
    *   - utiliser la méthode `compose` de l'instance `Functor` pour composer votre résultat
    *
    * Tips :
    *   - Seq n'est pas un functor
    *   - Les différentes météos sont dans l'énumération `Behaviour`
    *   - La méthode compose est en conflit avec les méthodes Scala, il serait peut-être logique d'instancier la type class `Functor` ??
    */
  def citiesNeedUmbrellaToday(cities: String*): Seq[Result[String, NeedUmbrella]] = {
    val weathers = service.getWeatherByCitiesSync(cities: _*).toList

    Functor[List].compose[Result[String, ?]].map[Weather, NeedUmbrella](weathers) { case Weather(behaviour, city) =>
      if (Behaviour.Rainy == behaviour) UnfoldUmbrella(city) else TakeFoldUmbrella(city)
    }
  }

  /**
    * L'objectif est de retourner la liste des noms des restaurants présents dans une ville
    *
    * Règles :
    *   - utiliser la méthode `getRestaurantAsync` du service
    *   - utiliser la méthode `compose` de l'instance `Functor` pour composer votre résultat
    *
    * Tips :
    *   - Hop !! hop !! on en enchaine encore les functors ensemble
    *   - myIO.map(_.map(_.map(_.name))) c'est pas jolie !
    *
    */
  def getRestaurantNameInTheCity(city: String): IO[Result[String, List[String]]] = {
    val restaurants: IO[Result[String, List[Restaurant]]] = service.getRestaurantAsync(city)

    Functor[IO]
      .compose[Result[String, ?]]
      .compose[List]
      .map[Restaurant, String](restaurants)(_.name)
  }

  println("-- City need umbrella --")
  println(citiesNeedUmbrellaToday("Paris", "Barcelone", "Montpellier"))
  println(citiesNeedUmbrellaToday("Paris", "Barcelone", "Montpellier").map(_.fold(_.asJson, _.asJson)))
  println("  ")
  println("-- Restaurants in the city --")
  println(getRestaurantNameInTheCity("Paris").unsafeRunSync())
  println(getRestaurantNameInTheCity("Barcelone").unsafeRunSync())
}

sealed trait NeedUmbrella {
  val city: City
}

case class UnfoldUmbrella(city: City) extends NeedUmbrella
case class TakeFoldUmbrella(city: City) extends NeedUmbrella
