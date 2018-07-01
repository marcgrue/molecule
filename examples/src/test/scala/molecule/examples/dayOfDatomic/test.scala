///*
//* AUTO-GENERATED Molecule DSL boilerplate code for namespace `Community`
//*
//* To change:
//* 1. edit schema definition file in `molecule.examples.seattle.schema/`
//* 2. `sbt compile` in terminal
//* 3. Refresh and re-compile project in IDE
//*/
//package molecule.examples.seattle.dsl
//package seattle
//import molecule.boilerplate._
//import molecule.boilerplate.attributes._
//import molecule.imports._
//
//
//object Community extends Community_0 with FirstNS {
//  def apply(eid: Long, eids: Long*): Community_0            = ???
//  def apply(eids: Iterable[Long])  : Community_0            = ???
//  def apply(eids: ?)               : Community_In_1_0[Long] = ???
//}
//
//
///** Namespace `Community` */
//trait Community {
//
//  /** A community's name
//    *
//    * Mandatory attribute `name`
//    *
//    * @inheritdoc
//    *
//    * @return String
//    * */
//  class name[Ns, In] extends OneString[Ns, In] with Indexed with FulltextSearch[Ns, In]
//
//  /** A community's url
//    *
//    * @return String
//    * */
//  class url[Ns, In] extends OneString[Ns, In] with Indexed
//
//  /** All community categories
//    *
//    * @return String
//    * */
//  class category[Ns, In] extends ManyString[Ns, In] with Indexed with FulltextSearch[Ns, In]
//
//  /** A community orgtype enum value
//    *
//    * @return String
//    * */
//  class orgtype[Ns, In] extends OneEnum[Ns, In] with Indexed { private lazy val community, commercial, nonprofit, personal = EnumValue }
//
//  /** Community type enum values
//    *
//    * Enums available:
//    * - email_list
//    * - twitter
//    * - facebook_page
//    * - blog
//    * - website
//    * - wiki
//    * - myspace
//    * - ning
//    *
//    * @return String
//    * */
//  class `type`[Ns, In] extends OneEnum[Ns, In] with Indexed { private lazy val email_list, twitter, facebook_page, blog, website, wiki, myspace, ning = EnumValue }
//
//  /** A community's neighborhood
//    *
//    * @return String
//    * */
//  class neighborhood[Ns, In] extends OneRefAttr[Ns, In] with Indexed
//
//
//  /** A community's name
//    *
//    * Optional attribute
//    *
//    * @return Option[String]
//    * */
//  class name$        [Ns, In] extends OneString$ [Ns] with Indexed with FulltextSearch[Ns, In]
//  class url$         [Ns, In] extends OneString$ [Ns] with Indexed
//  class category$    [Ns, In] extends ManyString$[Ns] with Indexed with FulltextSearch[Ns, In]
//  class orgtype$     [Ns, In] extends OneEnum$   [Ns] with Indexed { private lazy val community, commercial, nonprofit, personal = EnumValue }
//  class type$        [Ns, In] extends OneEnum$   [Ns] with Indexed { private lazy val email_list, twitter, facebook_page, blog, website, wiki, myspace, ning = EnumValue }
//  class neighborhood$[Ns, In] extends OneRefAttr$[Ns] with Indexed
//
//
//
//  /** A community's name
//    *
//    * Tacit attribute `name`
//    *
//    * @return String
//    * */
//  class name_[Ns, In] extends OneString[Ns, In] with Indexed with FulltextSearch[Ns, In]
//
//  /** A community's url
//    *
//    * Tacit attribute `url`
//    *
//    * @return String
//    * */
//  class url_[Ns, In] extends OneString[Ns, In] with Indexed
//
//  /** All community categories
//    *
//    * @return String
//    * */
//  class category_[Ns, In] extends ManyString[Ns, In] with Indexed with FulltextSearch[Ns, In]
//
//  /** A community orgtype enum value
//    *
//    * @return String
//    * */
//  class orgtype_[Ns, In] extends OneEnum[Ns, In] with Indexed { private lazy val community, commercial, nonprofit, personal = EnumValue }
//
//  /** Community type enum values
//    *
//    * Enums available:
//    * - email_list
//    * - twitter
//    * - facebook_page
//    * - blog
//    * - website
//    * - wiki
//    * - myspace
//    * - ning
//    *
//    * @return String
//    * */
//  class type_[Ns, In] extends OneEnum[Ns, In] with Indexed { private lazy val email_list, twitter, facebook_page, blog, website, wiki, myspace, ning = EnumValue }
//}
//
//trait Community_0 extends Community with Out_0[Community_0, Community_1, Community_In_1_0, Community_In_1_1] {
//  lazy val name          : name        [Community_1[String     ], Community_In_1_1[String     , String     ]] with Community_1[String     ] = ???
//  lazy val url           : url         [Community_1[String     ], Community_In_1_1[String     , String     ]] with Community_1[String     ] = ???
//  lazy val category      : category    [Community_1[Set[String]], Community_In_1_1[Set[String], Set[String]]] with Community_1[Set[String]] = ???
//  lazy val orgtype       : orgtype     [Community_1[String     ], Community_In_1_1[String     , String     ]] with Community_1[String     ] = ???
//  lazy val `type`        : `type`      [Community_1[String     ], Community_In_1_1[String     , String     ]] with Community_1[String     ] = ???
//  lazy val neighborhood  : neighborhood[Community_1[Long       ], Community_In_1_1[Long       , Long       ]] with Community_1[Long       ] = ???
//
//  lazy val name$         : name$        [Community_1[Option[String]     ], Community_In_1_1[Option[String]     , Option[String]     ]] with Community_1[Option[String]     ] = ???
//  lazy val url$          : url$         [Community_1[Option[String]     ], Community_In_1_1[Option[String]     , Option[String]     ]] with Community_1[Option[String]     ] = ???
//  lazy val category$     : category$    [Community_1[Option[Set[String]]], Community_In_1_1[Option[Set[String]], Option[Set[String]]]] with Community_1[Option[Set[String]]] = ???
//  lazy val orgtype$      : orgtype$     [Community_1[Option[String]     ], Community_In_1_1[Option[String]     , Option[String]     ]] with Community_1[Option[String]     ] = ???
//  lazy val type$         : type$        [Community_1[Option[String]     ], Community_In_1_1[Option[String]     , Option[String]     ]] with Community_1[Option[String]     ] = ???
//  lazy val neighborhood$ : neighborhood$[Community_1[Option[Long]       ], Community_In_1_1[Option[Long]       , Option[Long]       ]] with Community_1[Option[Long]       ] = ???
//
//  lazy val name_         : name_        [Community_0, Community_In_1_0[String     ]] with Community_0 = ???
//  lazy val url_          : url_         [Community_0, Community_In_1_0[String     ]] with Community_0 = ???
//  lazy val category_     : category_    [Community_0, Community_In_1_0[Set[String]]] with Community_0 = ???
//  lazy val orgtype_      : orgtype_     [Community_0, Community_In_1_0[String     ]] with Community_0 = ???
//  lazy val type_         : type_        [Community_0, Community_In_1_0[String     ]] with Community_0 = ???
//  lazy val neighborhood_ : neighborhood_[Community_0, Community_In_1_0[Long       ]] with Community_0 = ???
//
//  def Neighborhood  : OneRef [Community, Neighborhood] with Neighborhood_0 = ???
//}
//
//trait Community_1[A] extends Community with Out_1[Community_1, Community_2, Community_In_1_1, Community_In_1_2, A] {
//
//}