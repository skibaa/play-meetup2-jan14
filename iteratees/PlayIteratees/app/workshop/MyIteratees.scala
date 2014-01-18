package workshop

import play.api.libs.iteratee._

object MyIteratees {
  
  def consume( prevData:Seq[Int] ):Iteratee[Int, Seq[Int]] = Cont {
    case Input.El( i ) => consume( prevData :+ i )
    case Input.EOF => Done( prevData )
    case Input.Empty => consume( prevData )
  }
  
  val consumer = consume( Seq.empty )
  
  def sum( prevSum:Int ):Iteratee[Int, Int] = Done( prevSum )
  
  val summer = sum( 0 )
}