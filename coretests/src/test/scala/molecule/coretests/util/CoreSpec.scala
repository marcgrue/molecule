package molecule.coretests.util

import datomicScala.client.api.sync.{Client, Connection, Datomic}
import datomicScala.CognitectAnomaly
import molecule.core.facade.Conn
import molecule.core.util.MoleculeSpec
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.base.api
import molecule.datomic.peer.facade.Datomic_Peer
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}

class CoreSpec extends MoleculeSpec with CoreData {

  sealed trait System
  case object Peer extends System
  case object PeerServer extends System
  case object DevLocal extends System
  case object Cloud extends System

  private var system    : System     = Peer
  private var client    : Client     = null // set in setup
  private var connection: Connection = null // set in setup

  //  override def map(fs: => Fragments): Fragments = {
  //    step(setupPeer()) ^
  //      fs.mapDescription(d => Text(s"$system: " + d.show)) ^
  //      step(setupDevLocal()) ^
  //      fs.mapDescription(d => Text(s"$system: " + d.show)) ^
  //      step(setupPeerServer()) ^
  //      fs.mapDescription(d => Text(s"$system: " + d.show))
  //  }


  def setupPeer(): Unit = {
    system = Peer
  }

  def setupPeerServer(): Unit = {
    system = PeerServer
    client = Datomic.clientPeerServer("myaccesskey", "mysecret", "localhost:8998")
    connection = try {
      client.connect("hello")
    } catch {
      case e: CognitectAnomaly =>
        println(e)
        println(e.msg)
        throw e
      case t: Throwable        => throw t
    }
  }

  def setupDevLocal(): Unit = {
    system = DevLocal
    client = Datomic.clientDevLocal("Hello system name")
  }

  class CoreSetup extends Scope {
    // Entry point for Molecule to all systems
    implicit val conn: Conn = system match {
      case Peer       => Datomic_Peer.recreateDbFrom(CoreTestSchema)
      case PeerServer => Datomic_Peer.recreateDbFrom(CoreTestSchema)
      case DevLocal   => Datomic_Peer.recreateDbFrom(CoreTestSchema)
      case Cloud      => Datomic_Peer.recreateDbFrom(CoreTestSchema)
    }
  }


}

class CoreSpec1 extends CoreSpec with api.out1
class CoreSpec2 extends CoreSpec with api.out2
class CoreSpec3 extends CoreSpec with api.out3
class CoreSpec4 extends CoreSpec with api.out4
class CoreSpec5 extends CoreSpec with api.out5
class CoreSpec6 extends CoreSpec with api.out6
class CoreSpec7 extends CoreSpec with api.out7
class CoreSpec8 extends CoreSpec with api.out8
class CoreSpec9 extends CoreSpec with api.out9
class CoreSpec10 extends CoreSpec with api.out10
class CoreSpec11 extends CoreSpec with api.out11
class CoreSpec12 extends CoreSpec with api.out12
class CoreSpec13 extends CoreSpec with api.out13
class CoreSpec14 extends CoreSpec with api.out14
class CoreSpec15 extends CoreSpec with api.out15
class CoreSpec16 extends CoreSpec with api.out16
class CoreSpec17 extends CoreSpec with api.out17
class CoreSpec18 extends CoreSpec with api.out18
class CoreSpec19 extends CoreSpec with api.out19
class CoreSpec20 extends CoreSpec with api.out20
class CoreSpec21 extends CoreSpec with api.out21
class CoreSpec22 extends CoreSpec with api.out22

class CoreSpec1_1 extends CoreSpec with api.in1_out1
class CoreSpec1_2 extends CoreSpec with api.in1_out2
class CoreSpec1_3 extends CoreSpec with api.in1_out3
class CoreSpec1_4 extends CoreSpec with api.in1_out4
class CoreSpec1_5 extends CoreSpec with api.in1_out5
class CoreSpec1_6 extends CoreSpec with api.in1_out6
class CoreSpec1_7 extends CoreSpec with api.in1_out7
class CoreSpec1_8 extends CoreSpec with api.in1_out8
class CoreSpec1_9 extends CoreSpec with api.in1_out9
class CoreSpec1_10 extends CoreSpec with api.in1_out10
class CoreSpec1_11 extends CoreSpec with api.in1_out11
class CoreSpec1_12 extends CoreSpec with api.in1_out12
class CoreSpec1_13 extends CoreSpec with api.in1_out13
class CoreSpec1_14 extends CoreSpec with api.in1_out14
class CoreSpec1_15 extends CoreSpec with api.in1_out15
class CoreSpec1_16 extends CoreSpec with api.in1_out16
class CoreSpec1_17 extends CoreSpec with api.in1_out17
class CoreSpec1_18 extends CoreSpec with api.in1_out18
class CoreSpec1_19 extends CoreSpec with api.in1_out19
class CoreSpec1_20 extends CoreSpec with api.in1_out20
class CoreSpec1_21 extends CoreSpec with api.in1_out21
class CoreSpec1_22 extends CoreSpec with api.in1_out22

class CoreSpec2_1 extends CoreSpec with api.in2_out1
class CoreSpec2_2 extends CoreSpec with api.in2_out2
class CoreSpec2_3 extends CoreSpec with api.in2_out3
class CoreSpec2_4 extends CoreSpec with api.in2_out4
class CoreSpec2_5 extends CoreSpec with api.in2_out5
class CoreSpec2_6 extends CoreSpec with api.in2_out6
class CoreSpec2_7 extends CoreSpec with api.in2_out7
class CoreSpec2_8 extends CoreSpec with api.in2_out8
class CoreSpec2_9 extends CoreSpec with api.in2_out9
class CoreSpec2_10 extends CoreSpec with api.in2_out10
class CoreSpec2_11 extends CoreSpec with api.in2_out11
class CoreSpec2_12 extends CoreSpec with api.in2_out12
class CoreSpec2_13 extends CoreSpec with api.in2_out13
class CoreSpec2_14 extends CoreSpec with api.in2_out14
class CoreSpec2_15 extends CoreSpec with api.in2_out15
class CoreSpec2_16 extends CoreSpec with api.in2_out16
class CoreSpec2_17 extends CoreSpec with api.in2_out17
class CoreSpec2_18 extends CoreSpec with api.in2_out18
class CoreSpec2_19 extends CoreSpec with api.in2_out19
class CoreSpec2_20 extends CoreSpec with api.in2_out20
class CoreSpec2_21 extends CoreSpec with api.in2_out21
class CoreSpec2_22 extends CoreSpec with api.in2_out22

class CoreSpec3_1 extends CoreSpec with api.in3_out1
class CoreSpec3_2 extends CoreSpec with api.in3_out2
class CoreSpec3_3 extends CoreSpec with api.in3_out3
class CoreSpec3_4 extends CoreSpec with api.in3_out4
class CoreSpec3_5 extends CoreSpec with api.in3_out5
class CoreSpec3_6 extends CoreSpec with api.in3_out6
class CoreSpec3_7 extends CoreSpec with api.in3_out7
class CoreSpec3_8 extends CoreSpec with api.in3_out8
class CoreSpec3_9 extends CoreSpec with api.in3_out9
class CoreSpec3_10 extends CoreSpec with api.in3_out10
class CoreSpec3_11 extends CoreSpec with api.in3_out11
class CoreSpec3_12 extends CoreSpec with api.in3_out12
class CoreSpec3_13 extends CoreSpec with api.in3_out13
class CoreSpec3_14 extends CoreSpec with api.in3_out14
class CoreSpec3_15 extends CoreSpec with api.in3_out15
class CoreSpec3_16 extends CoreSpec with api.in3_out16
class CoreSpec3_17 extends CoreSpec with api.in3_out17
class CoreSpec3_18 extends CoreSpec with api.in3_out18
class CoreSpec3_19 extends CoreSpec with api.in3_out19
class CoreSpec3_20 extends CoreSpec with api.in3_out20
class CoreSpec3_21 extends CoreSpec with api.in3_out21
class CoreSpec3_22 extends CoreSpec with api.in3_out22