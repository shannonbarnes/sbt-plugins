import org.scalatest.{FlatSpec, Matchers}
import com.datastax.driver.core._

class TestSpec extends FlatSpec with Matchers {
   "Sample test" should "be able to connect to embedded Cassandra" in {
     val cluster = Cluster.builder().addContactPoint("localhost").withPort(9142).build()
     assert(cluster.getMetadata().getAllHosts().size == 1)
   }
}