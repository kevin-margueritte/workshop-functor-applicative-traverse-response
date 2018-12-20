package com.mrgueritte.core.result

import cats.Functor

object functorInstance extends ResultFunctor

trait ResultFunctor {

  implicit def resultFunctor[A, ?]: Functor[Result[A, ?]] = new Functor[Result[A, ?]] {
    override def map[B, BB](fa: Result[A, B])(f: B => BB): Result[A, BB] = {
      fa match {
        case Ok(value)     => Ok(f(value))
        case Error(value)  => Error(value)
      }
    }
  }

}

