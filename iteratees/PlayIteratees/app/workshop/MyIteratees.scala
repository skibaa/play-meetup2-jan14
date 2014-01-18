package workshop

import play.api.libs.iteratee._

object MyIteratees {
  
  def fold[A] ( start:A )( folder: Function2[A, Int, A]):Iteratee[Int, A] = Cont {
    case Input.El( i ) => fold( folder(start, i) )( folder )
    case Input.EOF => Done( start )
    case Input.Empty => fold( start )( folder )
  }
  
  def consume( prevData:Seq[Int] ):Iteratee[Int, Seq[Int]] = 
    fold( prevData ) { (prev, i) =>
	  prev :+ i
	}
  
  val consumer = consume( Seq.empty )
  
  def sum( prevSum:Int ):Iteratee[Int, Int] = 
  	fold ( prevSum ) { (prev, i) =>
      prev + i
    }
  
  val summer = sum( 0 )
}