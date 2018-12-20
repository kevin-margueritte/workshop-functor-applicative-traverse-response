# Semigroup & Monoid
## Semigroup
A `Semigroup` has a `binary operator`. Below the typeclass definition :

```scala
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
```
In this project we define the operator `max` which return the max between two values.

```scala
implicit val max = new Semigroup[Int] {
    override def combine(x: Int, y: Int): Int = if (x < y) x else y
  }
```

And the `syntactical sugar` :
```scala
implicit class SemigroupOps[A](x: A)(implicit semigroup: Semigroup[A]) {
    def |+|(y: A): A = semigroup.combine(x,y)
  }
```
Now we can call max in this way :
```scala
3 |+| 5
// Int: 5
```
## Monoid
A `Monoid` extends `Semigroup` and has an additional value `empty`

```scala
trait Monoid[A] extends Semigroup[A] {
  def empty: A
  def combineAll(l: List[A]): A = l.foldLeft(empty)((x,y) => combine(x,y))
}
```
For example, this value allows to implement a method `combineAll`, which allows to apply the `combine(x: A, y: A): A` in the elements from the list in parameter

## Run project
```sbt
$ sbt
$ test
```
