package workshop

import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global

object MyIteratees {
  
  def foldInt[A](a: A) = Iteratee.fold[Int, A](a)_
  
  val consumer = foldInt( Seq.empty[Int] )(_ :+ _)
  
  val summer = foldInt( 0 )(_ + _)
  
}