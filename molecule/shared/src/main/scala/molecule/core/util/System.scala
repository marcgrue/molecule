package molecule.core.util

sealed trait System

case object DatomicPeer extends System
case object DatomicPeerServer extends System
case object DatomicDevLocal extends System
//  case object DatomicCloud extends System // DevLocal should cover this