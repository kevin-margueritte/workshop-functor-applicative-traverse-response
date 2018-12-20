package com.mrgueritte.core.result

import cats.Applicative
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.PropertyChecks

class ApplicativeSpec extends FlatSpec with Matchers with PropertyChecks {
  import com.mrgueritte.core.result.applicativeInstance._

  "Result instance applicative" should
    "respect identity law" in {

    forAll { (a:Int) =>
      val ok: Result[Int, Int] = Ok(a)
      val error: Result[Int, Int] = Error(a)

      Applicative[Result[Int, ?]].<*>(Result[Int, Int => Int]((x: Int) => x))(ok) shouldBe(ok)
      Applicative[Result[Int, ?]].<*>(Result[Int, Int => Int]((x: Int) => x))(error) shouldBe(error)
    }

  }

  it must "respect composition law" in {
    forAll { (a: Int, b:Int, c: Int) =>

      val f: (Int => Int) = (y: Int) => y + b

      val applicativeInstance = Applicative[Result[Int, ?]]

      val apOk = applicativeInstance.<*>(applicativeInstance.pure(f))(applicativeInstance.pure(a))
      val pureOk = applicativeInstance.pure(f(a))

      apOk shouldBe(pureOk)
    }
  }

}
