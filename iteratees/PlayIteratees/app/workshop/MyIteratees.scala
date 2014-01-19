package workshop

import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object MyIteratees {
  
  def queryFromDb(i: Int): Future[String] = {
    Future.successful(i.toString)
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
          Step.Error("Not implemented", Input.Empty)
        }
        
        futureString flatMap stringToFutureB
      }
    }
    
    Cont(step)
  }
}