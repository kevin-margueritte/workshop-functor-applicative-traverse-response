package com.mrgueritte.core.database

import cats.effect.IO
import com.mrgueritte.core.model.City

object CityDAO extends FakeDatabase {

  def getAll(): IO[List[City]] =
    IO.pure(cityDb)

  def getCityByName(city: String): IO[Option[City]] =
    IO.pure(cityDb.find(_.name == city).headOption)

}
