package com.mrgueritte.core.result

//import com.mrgueritte.core.result.functorInstance._
import com.mrgueritte.core.result.applicativeInstance._

sealed trait Result[A, B] {

  val error = Result.ErrorProjection(this)
  val ok = Result.OkProjection(this)

  val isOk: Boolean
  val isError: Boolean = !isOk

  def fold[C](fa: A => C, fb: B => C): C = this match {
    case Ok(b) => fb(b)
    case Error(a)  => fa(a)
  }

  def map[C](f: B => C): Result[A, C] =
    resultApplicative.map(this)(f)

  def >*[C](fc: Result[A, C]): Result[A, C] =
    resultApplicative.*>(this)(fc)

  def <*[C](fc: Result[A, C]): Result[A, B] =
    resultApplicative.<*(this)(fc)

  def map2[C, Z](fc: Result[A, C])(f: (B, C) => Z): Result[A, Z] =
    resultApplicative.map2(this, fc)(f)
}

case class Ok[A, B](value: B) extends Result[A, B] {
  override val isOk: Boolean = true
}

case class Error[A, B](value: A) extends Result[A, B] {
  override val isOk: Boolean = false
}


object Result {

  def apply[A, B](v: B): Result[A, B] =
    resultApplicative.pure(v)

  def cond[A, B](test: Boolean, ok: => B, error: => A): Result[A, B] =
    if (test) Ok(ok) else Error(error)

  final case class ErrorProjection[A, B](result: Result[A, B]) {

    def get: A = {
      result match {
        case Error(e) => e
        case _ => throw new NoSuchElementException("Result.ok on Error")
      }
    }

  }

  final case class OkProjection[A, B](result: Result[A, B]) {

    def get: B = {
      result match {
        case Ok(result) => result
        case _ => throw new NoSuchElementException("Result.Error on Ok")
      }
    }

  }

}