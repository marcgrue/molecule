package molecule.datomic.base.util

sealed trait System

case object SystemPeer extends System
case object SystemPeerServer extends System
case object SystemDevLocal extends System
// case object SystemCloud extends System // DevLocal should cover this