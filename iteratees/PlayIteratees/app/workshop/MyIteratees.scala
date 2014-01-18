package workshop

import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MyIteratees {
  
  def foldInt[A](firstState: A) = Iteratee.fold2[Int, A](firstState) _
  
  def consumeUntil(p: Int => Future[Boolean]) = foldInt( Seq.empty[Int] ) {
    def step(prev:Seq[Int], i:Int) = p(i).map {
      case true => (prev, true)
      case false => (prev :+ i, false)
    }
    step
  }
  val consumeUntilZero = consumeUntil(i => Future.successful(i == 0))
  
  def groupBy[A](it:Iteratee[Int, A]): Iteratee[Int, Seq[A]] = ???
  
  val groupByZeros = groupBy(consumeUntilZero)
  
}