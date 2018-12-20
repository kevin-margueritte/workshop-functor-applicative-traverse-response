package com.mrgueritte.core.service

import com.mrgueritte.core.client.Client
import com.mrgueritte.core.model.Weather
import com.mrgueritte.core.result.Result

class ServiceTraverse {

  def getWeatherSync(city: String): Result[String, Weather] =
    Client.getWeatherSync(city)

}
