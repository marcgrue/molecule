package molecule.coretests.util

import molecule.core.schema.SchemaTransaction
import datomic.Util._
import datomic.Peer._

object CoreTestSchema_DevLocal extends SchemaTransaction {

  lazy val partitions = list()


  lazy val namespaces = list(

    // Ns ---------------------------------------------------------------

    map(read(":db/ident")             , read(":Ns/str"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/fulltext")          , true.asInstanceOf[Object],
      read(":db/doc")               , "Card one String attribute"
    ),

    map(read(":db/ident")             , read(":Ns/int"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/doc")               , "Card one Int attribute",
      ),

    map(read(":db/ident")             , read(":Ns/long"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/float"),
      read(":db/valueType")         , read(":db.type/double"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/double"),
      read(":db/valueType")         , read(":db.type/double"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/bool"),
      read(":db/valueType")         , read(":db.type/boolean"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/bigInt"),
      read(":db/valueType")         , read(":db.type/bigint"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/bigDec"),
      read(":db/valueType")         , read(":db.type/bigdec"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/date"),
      read(":db/valueType")         , read(":db.type/instant"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/uuid"),
      read(":db/valueType")         , read(":db.type/uuid"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/uri"),
      read(":db/valueType")         , read(":db.type/uri"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/enum"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum0")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum1")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum2")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum3")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum4")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum5")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum6")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum7")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum8")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enum/enum9")),

//    map(read(":db/ident")             , read(":Ns/byte"),
//      read(":db/valueType")         , read(":db.type/bytes"),
//      read(":db/cardinality")       , read(":db.cardinality/one"),
//      ),

    map(read(":db/ident")             , read(":Ns/parent"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/ref1"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ns/refSub1"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/isComponent")       , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ns/strs"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      read(":db/fulltext")          , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ns/ints"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/longs"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/floats"),
      read(":db/valueType")         , read(":db.type/double"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/doubles"),
      read(":db/valueType")         , read(":db.type/double"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/bools"),
      read(":db/valueType")         , read(":db.type/boolean"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/bigInts"),
      read(":db/valueType")         , read(":db.type/bigint"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/bigDecs"),
      read(":db/valueType")         , read(":db.type/bigdec"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/dates"),
      read(":db/valueType")         , read(":db.type/instant"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/uuids"),
      read(":db/valueType")         , read(":db.type/uuid"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/uris"),
      read(":db/valueType")         , read(":db.type/uri"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/enums"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum0")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum1")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum2")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum3")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum4")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum5")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum6")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum7")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum8")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ns.enums/enum9")),

//    map(read(":db/ident")             , read(":Ns/bytes"),
//      read(":db/valueType")         , read(":db.type/bytes"),
//      read(":db/cardinality")       , read(":db.cardinality/many"),
//      ),

    map(read(":db/ident")             , read(":Ns/parents"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/refs1"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/refsSub1"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      read(":db/isComponent")       , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ns/strMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      read(":db/fulltext")          , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ns/intMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/longMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/floatMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/doubleMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/boolMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/bigIntMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/bigDecMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/dateMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/uuidMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ns/uriMap"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

//    map(read(":db/ident")             , read(":Ns/byteMap"),
//      read(":db/valueType")         , read(":db.type/bytes"),
//      read(":db/cardinality")       , read(":db.cardinality/many"),
//      ),


    // Ref1 -------------------------------------------------------------

    map(read(":db/ident")             , read(":Ref1/str1"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/fulltext")          , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ref1/int1"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref1/enum1"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref1.enum1/enum10")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref1.enum1/enum11")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref1.enum1/enum12")),

    map(read(":db/ident")             , read(":Ref1/ref2"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref1/refSub2"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/isComponent")       , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ref1/strs1"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref1/ints1"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref1/enums1"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref1.enums1/enum10")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref1.enums1/enum11")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref1.enums1/enum12")),

    map(read(":db/ident")             , read(":Ref1/refs2"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref1/refsSub2"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      read(":db/isComponent")       , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ref1/intMap1"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),


    // Ref2 -------------------------------------------------------------

    map(read(":db/ident")             , read(":Ref2/str2"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/unique")            , read(":db.unique/identity"),
      read(":db/fulltext")          , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ref2/int2"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      read(":db/unique")            , read(":db.unique/value"),
      ),

    map(read(":db/ident")             , read(":Ref2/enum2"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref2.enum2/enum20")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref2.enum2/enum21")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref2.enum2/enum22")),

    map(read(":db/ident")             , read(":Ref2/strs2"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref2/ints2"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      read(":db/noHistory")         , true.asInstanceOf[Object],
      ),

    map(read(":db/ident")             , read(":Ref2/ref3"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref2/refs3"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),


    // Ref3 -------------------------------------------------------------

    map(read(":db/ident")             , read(":Ref3/str3"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref3/int3"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref3/enum3"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref3.enum3/enum30")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref3.enum3/enum31")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref3.enum3/enum32")),

    map(read(":db/ident")             , read(":Ref3/strs3"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref3/ints3"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref3/ref4"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref3/refs4"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),


    // Ref4 -------------------------------------------------------------

    map(read(":db/ident")             , read(":Ref4/str4"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref4/int4"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/ident")             , read(":Ref4/enum4"),
      read(":db/valueType")         , read(":db.type/ref"),
      read(":db/cardinality")       , read(":db.cardinality/one"),
      ),

    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref4.enum4/enum40")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref4.enum4/enum41")),
    map(read(":db/id"), tempid(":db.part/user"), read(":db/ident"), read(":Ref4.enum4/enum42")),

    map(read(":db/ident")             , read(":Ref4/strs4"),
      read(":db/valueType")         , read(":db.type/string"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      ),

    map(read(":db/ident")             , read(":Ref4/ints4"),
      read(":db/valueType")         , read(":db.type/long"),
      read(":db/cardinality")       , read(":db.cardinality/many"),
      )
  )
}