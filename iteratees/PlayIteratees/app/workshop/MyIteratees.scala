package workshop

import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object MyIteratees {
  
  def queryFromDb(i: Int): Future[String] = {
    Future.successful(i.toString)
  }
  
  //find first string in DB which starts from 3
  def find3: Iteratee[Int, String] = Cont {
    ???
  }
  
  private def i2iter(i: Int): Future[Iteratee[Int,String]] = 
    queryFromDb(i) map { s =>
      if (s.startsWith("3"))
        ???
      else
        ???
    }
}