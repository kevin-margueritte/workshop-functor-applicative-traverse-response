package com.mrgueritte.exercice.applicative

import cats.Applicative
import cats.effect.IO
import com.mrgueritte.core.model.{Restaurant, Weather}
import com.mrgueritte.core.result.Result
import com.mrgueritte.core.service.ServiceApplicative


object Process extends App {
  import com.mrgueritte.core.result.applicativeInstance._

  val service = new ServiceApplicative

  /**
    * L'objectif est de retourner la météo des deux villes passées en paramètres
    *
    * Règles :
    *   - utiliser la méthode `getWeatherAsync` du service
    *
    * Tips:
    *   - souvenez vous qu'une `Applicative` est aussi un `Functor`, ça peut se composer
    *   - regardez la méthode `product` de l'`Applicative`
    *
    */
  def getWeatherTwoCities(cityA: String, cityB: String): IO[Result[String, (Weather, Weather)]] = {
    val weatherCityA: IO[Result[String, Weather]] = service.getWeatherAsync(cityA)
    val weatherCityB: IO[Result[String, Weather]] = service.getWeatherAsync(cityB)


    Applicative[IO].compose[Result[String, ?]].product(weatherCityA, weatherCityB)
  }

  /**
    * L'objectif est de retourner la météo des trois villes passées en paramètres
    *
    * Règles :
    *   - utiliser la méthode `getWeatherAsync` du service
    *
    * Tips:
    *   - souvenez vous qu'une `Applicative` est aussi un `Functor`, ça peut se composer
    *   - regardez la méthode `product` de l'`Applicative`
    *
    */
  def getWeatherThreeCities(cityA: String, cityB: String, cityC: String): IO[Result[String, (Weather, Weather, Weather)]] = {
    val weatherCityA: IO[Result[String, Weather]] = service.getWeatherAsync(cityA)
    val weatherCityB: IO[Result[String, Weather]] = service.getWeatherAsync(cityB)
    val weatherCityC: IO[Result[String, Weather]] = service.getWeatherAsync(cityC)


    Applicative[IO].compose[Result[String, ?]].tuple3(weatherCityA, weatherCityB, weatherCityC)
  }

  /**
    * L'objectif est de retourner une liste `Restaurant` contenant tous les restaurants de la ville passée en paramètre
    *
    * Règles :
    *   - utiliser la méthode `getRestaurantSync` du service
    *   - utiliser la méthode `isRain` du service
    *   - l'utilisation `for-comprehension`, la méthode `map` et `flatMap` est interdit
    *   - utiliser `product` ou `<*` ou `>*` de l'`Applicative`
    *
    */
  def whereDoIGoSync(city: String): Result[String, List[Restaurant]] = {
    val restaurants: Result[String, List[Restaurant]] = service.getRestaurantSync(city)
    val isRain: Result[String, Weather] = service.isRain(city)

    isRain >* restaurants
  }

  /**
    * L'objectif est de retourner une liste `Restaurant` contenant tous les restaurants de la ville passée en paramètre
    * et d'envoyer un message personnalisé à l'utilisateur
    *
    * Règles :
    *   - utiliser la méthode `getRestaurantSync` du service
    *   - utiliser la méthode `isRain` du service
    *   - utiliser la méthode `prettyRestaurant` du service
    *   - l'utilisation `for-comprehension`, la méthode `map` et `flatMap` est interdit
    *   - utiliser `map2` de l'`Applicative`
    *
    */
  def whereDoIGoPrettySync(city: String): Result[String, String] = {
    val restaurants: Result[String, List[Restaurant]] = service.getRestaurantSync(city)
    val isRain: Result[String, Weather] = service.isRain(city)

    restaurants.map2(isRain)(service.prettyRestaurant)
  }

  /**
    * Un peu plus de challenge !!
    *
    * L'objectif est de retourner une liste `Restaurant` contenant tous les restaurants de la ville passée en paramètre
    * et d'envoyer un message personnalisé à l'utilisateur
    *
    * Règles :
    *   - utiliser la méthode `getRestaurantSync` du service
    *   - utiliser la méthode `isRainAsync` du service
    *   - utiliser la méthode `prettyRestaurant` du service
    *   - l'utilisation `for-comprehension`, la méthode `map` et `flatMap` est interdit
    *   - utiliser `map2` de l'`Applicative`
    *
    * Tips:
    *   - souvenez vous qu'une `Applicative` est aussi un `Functor`, ça peut se composer
    *
    */
  def whereDoIGoPrettyAsync(city: String): IO[Result[String, String]] = {
    val restaurantsAsync: IO[Result[String, List[Restaurant]]] = service.getRestaurantAsync(city)
    val isRainAsync: IO[Result[String, Weather]] = service.isRainAsync(city)

    Applicative[IO].compose[Result[String, ?]].map2(restaurantsAsync, isRainAsync)(service.prettyRestaurant)
  }

  /**
    *
    * L'objectif est de retourner la météo de toutes les villes passées en paramètre
    *
    * Tips:
    *   - la méthode `tupleN` existe-t-elle ?
    *   - l'`Applicative` premet-elle de faire ce genre de chose ???
    *   - si vous n'y arrivez toujours pas, c'est peut-être impossible ???
    *
    */
  def getWeatherNCities(cities: String*): IO[Result[String, List[Weather]]] = ???

  println("-- Get weather two cities --")
  println(getWeatherTwoCities("Paris", "Montpellier").unsafeRunSync())
  println(" - with an error - ")
  println(getWeatherTwoCities("Paris", "Barcelone").unsafeRunSync())
  println(" ")
  println("-- Get weather three cities --")
  println(getWeatherThreeCities("Paris", "Montpellier", "Toulouse").unsafeRunSync())
  println(" - with an error - ")
  println(getWeatherThreeCities("Paris", "Barcelone", "Toulouse").unsafeRunSync())
  println(" ")
  println("-- Where do I go --")
  println(whereDoIGoSync("Montpellier"))
  println(" - with error - ")
  println(whereDoIGoSync("Paris"))
  println(" ")
  println("-- Where do I go: pretty synchrone mode --")
  println(whereDoIGoPrettySync("Montpellier"))
  println(" - with error - ")
  println(whereDoIGoPrettySync("Paris"))
  println(" ")
  println("-- Where do I go: pretty assynchrone mode --")
  println(whereDoIGoPrettyAsync("Montpellier").unsafeRunSync())
  println(" - with error - ")
  println(whereDoIGoPrettyAsync("Paris").unsafeRunSync())
}
