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
  
  val e = Enumerator[Int] (1, 2, 31, 0, 4)
  
  def feedAndGet[A] (it:Iteratee[Int, A]):A = {
    import scala.concurrent.Await
    import scala.concurrent.duration._
    
    val futureA = e.run(it)
    Await.result(futureA, Duration.Inf)
  }

  "MyIteratees" should {

    "group input by sequences until zero" in {
      feedAndGet(MyIteratees.find3) must_== "31"
    }

  }
}
