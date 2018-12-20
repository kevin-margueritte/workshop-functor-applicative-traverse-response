package com.mrgueritte.exercice.traverse

import cats.{Applicative, Eval, Traverse}


sealed trait ListZ[+A] {

  import ListZ._

  def drop(n: Int): ListZ[A] = {
    this match {
      case Empty => Empty
      case Cons(hd, tail) if n > 0 => tail.drop(n - 1)
      case Cons(hd, tail) if n <= 0 => Cons(hd, tail)
    }
  }

  def headOption(): Option[A] = {
    this match {
      case Empty => None
      case Cons(hd, _) => Some(hd)
    }
  }

  def ::[B >: A](value: B): ListZ[B] =
    Cons(value, this)

  def foldLeft[B](acc: B)(f: (B, A) => B): B =
    traverseInstance.foldLeft(this, acc)(f)

  def traverse[G[_], B](f: A => G[B])(implicit applicative: Applicative[G]): G[ListZ[B]] =
    traverseInstance.traverse(this)(f)

}

case object Empty extends ListZ[Nothing]
case class Cons[+A](hd: A, tail: ListZ[A]) extends ListZ[A]

object ListZ {

  def apply[A](as: A*): ListZ[A] = {
    as.isEmpty match {
      case true => Empty
      case false => Cons(as.head, apply(as.tail: _*))
    }
  }

  implicit def traverseInstance: Traverse[ListZ] = new Traverse[ListZ] {
    override def traverse[G[_], A, B](fa: ListZ[A])(f: A => G[B])(implicit applicative: Applicative[G]): G[ListZ[B]] = {
      foldLeft(fa, applicative.pure(Empty: ListZ[B])) { case (acc, a) =>
        val fb: G[B] = f(a)
        applicative.map2[ListZ[B], B, ListZ[B]](acc, fb) { case (list, b) => b :: list }
      }
    }

    override def foldLeft[A, B](fa: ListZ[A], acc: B)(f: (B, A) => B): B = {
      fa match {
        case Empty => acc
        case Cons(hd, tail) => foldLeft(tail, f(acc, hd))(f)
      }
    }

    override def foldRight[A, B](fa: ListZ[A], acc: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
      fa match {
        case Empty => acc
        case Cons(hd, tail) => foldRight(tail, f(hd, acc))(f)
      }
    }
  }

}
