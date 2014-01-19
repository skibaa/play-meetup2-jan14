package workshop

import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object MyIteratees {
  
  def queryFromDb(i: Int): Future[String] = {
    Future.successful(i.toString)
  }
  
  def secondQuery(s:String): Future[Int] = {
    Future.successful(s.length)
  }
  
  //find first string in DB which starts from 3
  def find3: Iteratee[Int, String] = Cont {
    case Input.Empty => find3
    case Input.EOF => Done("")
    case Input.El(i) => Iteratee flatten i2iter(i)
  }
  
  private def i2iter(i: Int): Future[Iteratee[Int,String]] = 
    queryFromDb(i) map { s =>
      if (s.startsWith("3"))
        Done(s)
      else
        find3
    }
  
  //find first string in DB which causes second query to return 4
  def findLen4: Iteratee[Int, String] = Cont {
    case Input.Empty => find3
    case Input.EOF => Done("")
    case Input.El(i) => Iteratee.flatten {
      for {
        s <- queryFromDb(i)
        l <- secondQuery(s)
      } yield {
        if (l==4)
          Done(s)
        else
          findLen4
      }
    }
  }
  
  def find3AfterLen4: Iteratee[Int, String] = 
    for {
      _ <- findLen4
      s <- find3
    }
      yield s
}