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
  def find3: Iteratee[Int, String] = {
    
    def step: Input[Int] => Iteratee[Int, String] = {
      case Input.Empty => find3
      case Input.EOF => Done("")
      case Input.El(i) => Find3Iteratee(i)
    }
    
    case class Find3Iteratee(prevInput:Int) extends Iteratee[Int, String] {
      type C = String
      val futureString:Future[C] = queryFromDb(prevInput)
      
      def fold[B](folder: Step[Int, String] => Future[B])(implicit ec: ExecutionContext): Future[B] = { 
        def stringToFutureB (stringFromDb:C): Future[B] = folder {
          if (stringFromDb.startsWith("3"))
            Step.Done(stringFromDb, Input.Empty)
          else
            Step.Cont(step)
        }
        
        futureString flatMap stringToFutureB
      }
    }
    
    Cont(step)
  }
  
   //find first string in DB which causes second query to return 4
  def findLen4: Iteratee[Int, String] = {
    
    def step: Input[Int] => Iteratee[Int, String] = {
      case Input.Empty => findLen4
      case Input.EOF => Done("")
      case Input.El(i) => FindLen4Iteratee(i)
    }
    
    case class FindLen4Iteratee(prevInput:Int) extends Iteratee[Int, String] {
      def fold[B](folder: Step[Int, String] => Future[B])(implicit ec: ExecutionContext): Future[B] = {
        def res2futureB(firstRes:String, secondRes:Int): Future[B] = folder {
          if (secondRes == 4)
            Step.Done(firstRes, Input.Empty)
          else
            Step.Cont(step)
        }
        
        for {
          firstRes <- queryFromDb(prevInput)
          secondRes <- secondQuery(firstRes)
          b <- res2futureB(firstRes, secondRes)
        }
          yield b
      }
    }
    
    Cont(step)
  }
}