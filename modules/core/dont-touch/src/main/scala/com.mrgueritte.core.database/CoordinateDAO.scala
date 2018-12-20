package com.mrgueritte.core.database

import cats.effect.IO
import com.mrgueritte.core.model.City

object CoordinateDAO extends FakeDatabase {

  def getCityByCoordinate(lat: BigDecimal, lon: BigDecimal): IO[Option[City]] = {
    IO.pure {
      cityDb.find { case City(_, c) =>
        if (c.lat == lat && c.lon == lon) true else false
      }.headOption
    }
  }

}
