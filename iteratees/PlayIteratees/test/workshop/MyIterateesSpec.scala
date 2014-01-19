package workshop

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

import play.api.libs.iteratee._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class MyIterateesSpec extends Specification {
  
  val e = Enumerator[Int] (1, 2, 31, 1567, 4)
  
  def feedAndGet[A] (it:Iteratee[Int, A]):A = {
    import scala.concurrent.Await
    import scala.concurrent.duration._
    
    val futureA = e.run(it)
    Await.result(futureA, Duration.Inf)
  }

  "MyIteratees" should {

    "find first string in DB starting from 3" in {
      feedAndGet(MyIteratees.find3) must_== "31"
    }

    "find first string in DB for which second query returns 4" in {
      feedAndGet(MyIteratees.findLen4) must_== "1567"
    }
  }
}
