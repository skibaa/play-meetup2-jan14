package workshop

import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object MyIteratees {
  
  def queryFromDb(i: Int): Future[String] = {
    Future.successful(i.toString)
  }
  
  def find3: Iteratee[Int, String] = ???
  
}