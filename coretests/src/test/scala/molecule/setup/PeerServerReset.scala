package molecule.setup

import datomic.Peer
import org.specs2.mutable.Specification


class PeerServerReset extends Specification {
  sequential

  // 1. Stop Peer Server process (ctrl-c)
  // 2. Run both tests here to reset Peer Server hello db
  // 3. Start Peer Server:
  //    bin/run -m datomic.peer-server -h localhost -p 8998 -a myaccesskey,mysecret -d hello,datomic:dev://localhost:4334/hello

  "delete hello db" >> {
    // Run this test only to delete test db 'hello'
    Peer.deleteDatabase("datomic:dev://localhost:4334/coretests") // pro
    //    Peer.deleteDatabase("datomic:free://localhost:4334/hello") // free
    ok
  }

  "Reset hello db" >> {
    Peer.createDatabase("datomic:dev://localhost:4334/coretests") // pro
    //    Peer.createDatabase("datomic:free://localhost:4334/hello") // free
    ok
  }
}
