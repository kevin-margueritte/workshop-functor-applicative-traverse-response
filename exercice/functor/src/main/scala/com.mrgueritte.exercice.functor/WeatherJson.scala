package com.mrgueritte.exercice.functor

trait WeatherJson {
  import io.circe.Encoder

  implicit val needUmbrellaEncoder: Encoder[NeedUmbrella] = Encoder[String].contramap[NeedUmbrella] {
    case UnfoldUmbrella(city)   => s"In ${city.name} take your umbrella !!"
    case TakeFoldUmbrella(city) => s"In ${city.name} take your sunglasses !!"
  }
}

object WeatherEncoder extends WeatherJson

