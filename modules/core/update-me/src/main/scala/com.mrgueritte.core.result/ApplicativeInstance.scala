package com.mrgueritte.core.result

import cats.Applicative

object applicativeInstance extends ResultApplicative

trait ResultApplicative {

  implicit def resultApplicative[A, ?]: Applicative[Result[A, ?]] = new Applicative[Result[A, ?]] {
    override def pure[B](x: B): Result[A, B] = Ok(x)

    override def ap[B, Z](ff: Result[A, B => Z])(fa: Result[A, B]): Result[A, Z] = {
      fa match {
        case err @ Error(_) => err.asInstanceOf[Result[A, Z]]
        case Ok(value) =>
          ff match {
            case Ok(f) => Ok(f(value))
            case err @ Error(_) => err.asInstanceOf[Result[A, Z]]
          }
      }
    }
  }

}