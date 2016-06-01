package molecule
package examples.seattle
import java.io.FileReader
import datomic.{Connection, Peer, Util}
import molecule.examples.seattle.dsl.seattle._
import molecule.examples.seattle.schema.SeattleSchema
import molecule.util.MoleculeSpec
import org.specs2.specification.Scope

//import org.specs2.control.NoLanguageFeatures


trait SeattleSpec extends MoleculeSpec with DatomicFacade {

  def loadFromFiles(schemaFile: String, dataFile: String, version: Int) = {
    val uri = "datomic:mem://seattle" + version
    Peer.deleteDatabase(uri)
    Peer.createDatabase(uri)
    implicit val conn = Peer.connect(uri)

    val dataDir = "examples/resources/seattle/"
    val schema_rdr = new FileReader(dataDir + schemaFile)
    val schema_tx = Util.readAll(schema_rdr).get(0).asInstanceOf[java.util.List[_]]
    conn.transact(schema_tx).get()

    val data_rdr = new FileReader(dataDir + dataFile)
    val data_tx = Util.readAll(data_rdr).get(0).asInstanceOf[java.util.List[_]]
    conn.transact(data_tx).get()

    conn
  }

//  class SeattleSetup extends Scope with NoLanguageFeatures with DatomicFacade {
  class SeattleSetup extends Scope with DatomicFacade {
    implicit val conn = recreateDbFrom(SeattleSchema)
    // Insert data
    //    Community.name.url.`type`.orgtype$.category$.Neighborhood.name.District.name.region$ insert seattleData0
    Community.name.url.`type`.orgtype$.category$.Neighborhood.name.District.name.region$ insert seattleData
  }

  def loadSeattle(version: Int): Connection = {
    implicit val conn = recreateDbFrom(SeattleSchema, "resources/seattle" + version)
    // Insert data
    Community.name.url.`type`.orgtype$.category$.Neighborhood.name.District.name.region$ insert seattleData

    conn
  }


  implicit val conn = loadSeattle(1)

  lazy val seattleData0 = List(
    ("15th Ave Community", "http://groups.yahoo.com/group/15thAve_Community/", "email_list", Some("community"), Some(Set("15th avenue residents", "xx")), "Capitol Hill", "East", Some("e")),
    ("BikeWorks!", "http://www.bikeworks.org/", "website", None, None, "Columbia City", "Southeast", Some("se")),
    ("belltown", "http://www.belltownpeople.com/", "blog", Some("commercial"), Some(Set("news", "events")), "Belltown", "Downtown", Some("w"))
  )

  lazy val seattleData = List(
    ("15th Ave Community", "http://groups.yahoo.com/group/15thAve_Community/", "email_list", Some("community"), Some(Set("15th avenue residents")), "Capitol Hill", "East", Some("e")),
    ("Admiral Neighborhood Association", "http://groups.yahoo.com/group/AdmiralNeighborhood/", "email_list", Some("community"), Some(Set("neighborhood association")), "Admiral (West Seattle)", "Southwest", Some("sw")),
    ("Alki News", "http://groups.yahoo.com/group/alkibeachcommunity/", "email_list", Some("community"), Some(Set("members of the Alki Community Council and residents of the Alki Beach neighborhood")), "Alki", "Southwest", Some("sw")),
    ("Alki News/Alki Community Council", "http://alkinews.wordpress.com/", "blog", Some("community"), Some(Set("news", "council meetings")), "Alki", "Southwest", Some("sw")),
    ("All About Belltown", "http://www.belltown.org/", "website", Some("community"), Some(Set("community council")), "Belltown", "Downtown", Some("w")),
    ("All About South Park", "http://www.allaboutsouthpark.com/", "website", Some("community"), Some(Set("neighborhood info")), "South Park", "Greater Duwamish", Some("s")),
    ("ArtsWest", "http://www.artswest.org/?q=node/28", "website", Some("community"), Some(Set("arts")), "West Seattle", "Southwest", Some("sw")),
    ("At Large in Ballard", "http://blog.seattlepi.com/ballard/", "blog", Some("commercial"), Some(Set("news", "human interest")), "Ballard", "Ballard", Some("nw")),
    ("Aurora Seattle", "http://www.auroraseattle.com/", "blog", Some("community"), Some(Set("news", "traffic", "planning")), "Regional Sites", "Northeast", Some("ne")),
    ("Ballard Avenue", "http://www.ballardavenue.blogspot.com/", "blog", Some("community"), Some(Set("personal ballard-centric blog")), "Ballard", "Ballard", Some("nw")),
    ("Ballard Blog", "http://ballardblog.com/", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Ballard", "Ballard", Some("nw")),
    ("Ballard Chamber of Commerce", "http://www.ballardchamber.com/", "website", Some("community"), Some(Set("chamber of commerce")), "Ballard", "Ballard", Some("nw")),
    ("Ballard District Council", "http://www.ballarddistrictcouncil.org/", "website", Some("community"), Some(Set("district council")), "Ballard", "Ballard", Some("nw")),
    ("Ballard Gossip Girl", "http://www.ballardgossipgirl.com/", "blog", Some("commercial"), Some(Set("nightlift", "shopping", "restaurants")), "Ballard", "Ballard", Some("nw")),
    ("Ballard Historical Society", "http://www.ballardhistory.org/", "website", Some("community"), Some(Set("historical society")), "Ballard", "Ballard", Some("nw")),
    ("Ballard Moms", "http://groups.yahoo.com/group/BallardMoms/", "email_list", Some("community"), Some(Set("Ballard parents")), "Ballard", "Ballard", Some("nw")),
    ("Ballard Neighbor Connection", "http://groups.yahoo.com/group/BallardNeighborConnection/", "email_list", Some("community"), Some(Set("neighborhood residents")), "Ballard", "Ballard", Some("nw")),
    ("Beach Drive Blog", "http://www.mortgageporter.com/beach_drive/", "blog", Some("commercial"), Some(Set("home sales", "crime", "news")), "Alki", "Southwest", Some("sw")),
    ("Beacon Hill Alliance of Neighbors", "http://www.cityofseattle.net/ban/", "website", Some("community"), Some(Set("public safety", "community")), "Beacon Hill", "Greater Duwamish", Some("s")),
    ("Beacon Hill Blog", "http://beaconhill.seattle.wa.us/", "blog", Some("commercial"), Some(Set("events", "food", "nightlife", "criminal activity", "news")), "Beacon Hill", "Greater Duwamish", Some("s")),
    ("Beacon Hill Burglaries", "http://maps.google.com/maps/ms?ie=UTF8&hl=en&msa=0&msid=107398592337461190820.000449fcf97ff8bfbe281&z=14or", "email_list", Some("community"), Some(Set("criminal activity")), "Beacon Hill", "Greater Duwamish", Some("s")),
    ("Beacon Hill Community Site", "http://beaconhillcommunity.wetpaint.com/", "wiki", Some("commercial"), Some(Set("community concerns", "announcements", "news")), "Beacon Hill", "Greater Duwamish", Some("s")),

    // Manually added (two missing values)
    ("BikeWorks!", "http://www.bikeworks.org/", "website", None, None, "Columbia City", "Southeast", Some("se")),

    ("Blogging Georgetown", "http://www.blogginggeorgetown.com/", "blog", Some("commercial"), Some(Set("911 blotter", "news", "events")), "Georgetown", "Greater Duwamish", Some("s")),
    ("Blogging Georgetown", "http://www.facebook.com/blogginggeorgetown", "facebook_page", Some("community"), Some(Set("911 blotter", "news", "events")), "Georgetown", "Greater Duwamish", Some("s")),
    ("Broadview Community Council", "http://groups.google.com/group/broadview-community-council", "email_list", Some("community"), Some(Set("community council")), "Broadview", "Northwest", Some("sw")),
    ("Broadview Community Council", "http://www.broadviewseattle.org/", "blog", Some("community"), Some(Set("community council")), "Broadview", "Northwest", Some("sw")),
    ("CHS Capitol Hill Seattle Blog", "http://www.capitolhillseattle.com/", "blog", Some("commercial"), Some(Set("drink", "events", "food", "criminal activity", "news")), "Capitol Hill", "East", Some("e")),
    ("Capitol Hill Community Council", "http://chcc.wikidot.com/", "wiki", Some("community"), Some(Set("community council", "news", "events")), "Capitol Hill", "East", Some("e")),
    ("Capitol Hill Housing", "http://capitolhillhousing.org/", "website", Some("community"), Some(Set("affordable housing")), "Capitol Hill", "East", Some("e")),
    ("Capitol Hill Triangle", "http://chtriangle.blogspot.com/", "blog", Some("commercial"), Some(Set("local miscellany")), "Capitol Hill", "East", Some("e")),
    ("Central Area Community Festival Association", "http://www.cacf.com/", "website", Some("community"), Some(Set("festival planning association")), "Central District", "Central", Some("e")),
    ("Central Ballard Community Council", "http://www.neighborhoodlink.com/seattle/cbcc/", "website", Some("community"), Some(Set("community council")), "Ballard", "Ballard", Some("nw")),
    ("Central District News", "http://www.centraldistrictnews.com/", "blog", Some("commercial"), Some(Set("drink", "events", "food", "reviews", "news")), "Central District", "Central", Some("e")),
    ("Chinatown/International District", "http://www.cidbia.org/", "website", Some("community"), Some(Set("business improvement association")), "International District", "Downtown", Some("w")),
    ("Chinese Information and Service Center", "http://www.cisc-seattle.org/", "website", Some("nonprofit"), Some(Set("support services", "advocacy", "referral")), "International District", "Downtown", Some("w")),
    ("Columbia Citizens", "http://columbiacitizens.net/", "wiki", Some("community"), Some(Set("favorite places", "neighborhood community site; buying/selling", "restaurants", "etc.")), "Columbia City", "Southeast", Some("se")),
    ("Columbia Citizens", "http://twitter.com/CCitizens", "twitter", Some("community"), Some(Set("twitter for Columbia Citizens")), "Columbia City", "Southeast", Some("se")),
    ("Columbia Citizens", "http://www.facebook.com/pages/Columbia-Citizens/48558627705", "facebook_page", Some("community"), Some(Set("favorite places", "neighborhood community site; buying/selling", "restaurants", "etc.")), "Columbia City", "Southeast", Some("se")),
    ("Columbia City Blog", "http://www.columbiacityblog.com/", "blog", Some("commercial"), Some(Set("shopping", "news", "events", "food")), "Columbia City", "Southeast", Some("se")),
    ("Columbia City, Seattle", "http://www.columbiacityseattle.com/", "website", Some("community"), Some(Set("business association")), "Columbia City", "Southeast", Some("se")),
    ("Columbia City: Rainier Valley", "http://www.rainiervalley.org/", "website", Some("community"), Some(Set("portal")), "Rainier Valley", "Southeast", Some("se")),
    ("Community Harvest of Southwest Seattle", "http://gleanit.org", "website", Some("community"), Some(Set("sustainable food")), "West Seattle", "Southwest", Some("sw")),
    ("Crown Hill Neighbors", "http://www.crownhillneighbors.org", "website", Some("community"), Some(Set("news", "neighborhood-issues", "neighborhood-planning")), "Crown Hill", "Ballard", Some("nw")),
    ("Delridge Grassroots Leadership", "http://delridge.blogspot.com/", "website", Some("community"), Some(Set("community organization")), "Delridge", "Delridge", Some("sw")),
    ("Delridge Neighborhoods Development Association", "http://www.dnda.org/", "website", Some("community"), Some(Set("neighborhood association")), "Delridge", "Delridge", Some("sw")),
    ("Delridge Produce Cooperative", "http://sites.google.com/site/delridgeproducecooperative/", "website", Some("community"), Some(Set("produce coop")), "Delridge", "Delridge", Some("sw")),
    ("Discover SLU", "http://www.discoverslu.com", "website", Some("commercial"), Some(Set("shopping", "news", "events", "dining")), "South Lake Union", "Lake Union", Some("w")),
    ("Discover SLU", "http://www.facebook.com/discoverslu", "facebook_page", Some("commercial"), Some(Set("shopping", "news", "events", "dining")), "South Lake Union", "Lake Union", Some("w")),
    ("Discover SLU", "http://www.twitter.com/southlakeunion", "twitter", Some("commercial"), Some(Set("shopping", "news", "events", "dining")), "South Lake Union", "Lake Union", Some("w")),
    ("Downtown Dispatch", "http://downtowndispatch.com/", "blog", Some("commercial"), Some(Set("news and events")), "Downtown", "Downtown", Some("w")),

    // Manually added (two missing values)
    ("Downtown Seattle Association", "http://www.downtownseattle.com/", "website", None, Some(Set("business association")), "Downtown", "Downtown", None),

    ("East Ballard Community Association Blog", "http://eastballard.wordpress.com/", "blog", Some("community"), Some(Set("community association", "news", "events", "meeting")), "Ballard", "Ballard", Some("nw")),
    ("Eastlake Ave. ", "http://eastlakeave.neighborlogs.com/", "blog", Some("commercial"), Some(Set("criminal activity", "news", "events", "traffic")), "Eastlake", "Lake Union", Some("w")),
    ("Eastlake Ave. ", "http://twitter.com/eastlakeave", "blog", Some("commercial"), Some(Set("updates on blog posts")), "Eastlake", "Lake Union", Some("w")),
    ("Eastlake Community Council", "http://www.eastlakeseattle.org/", "website", Some("community"), Some(Set("community council")), "Eastlake", "Lake Union", Some("w")),
    ("Eastlake Community Council", "http://www.facebook.com/group.php?gid=7145111961", "facebook_page", Some("community"), Some(Set("community council")), "Eastlake", "Lake Union", Some("w")),
    ("Fauntleroy Community Association", "http://fauntleroy.net/", "website", Some("community"), Some(Set("community association")), "Fauntleroy", "Southwest", Some("sw")),
    ("Fauntleroy Community Association", "http://www.facebook.com/pages/Seattle-WA/Fauntleroy-Community-Association/63181596775?v=wall&viewas=1779772562&ref=ts", "facebook_page", Some("community"), Some(Set("community assocation")), "Fauntleroy", "Southwest", Some("sw")),
    ("First Hill Improvement Association", "http://www.firsthill.org/", "website", Some("community"), Some(Set("neighborhood association")), "First Hill", "East", Some("e")),
    ("Fremont Arts Council", "http://groups.yahoo.com/group/fremontartscouncil/", "email_list", Some("community"), Some(Set("fremont arts council members")), "Fremont", "Lake Union", Some("w")),
    ("Fremont Arts Council", "http://www.fremontartscouncil.org/", "website", Some("community"), Some(Set("communtiy group")), "Fremont", "Lake Union", Some("w")),
    ("Fremont Chamber of Commerce", "http://www.fremontseattle.com/", "website", Some("community"), Some(Set("chamber of commerce")), "Fremont", "Lake Union", Some("w")),
    ("Fremont Universe", "http://twitter.com/fremontuniverse", "twitter", Some("commercial"), Some(Set("news", "events", "reviews", "food")), "Fremont", "Lake Union", Some("w")),
    ("Fremont Universe", "http://www.facebook.com/pages/Fremont-Universe-Seattle/88279594341?ref=s", "facebook_page", Some("commercial"), Some(Set("news", "events", "reviews", "food")), "Fremont", "Lake Union", Some("w")),
    ("Fremont Universe", "http://www.fremontuniverse.com/", "blog", Some("commercial"), Some(Set("news", "events", "reviews", "food")), "Fremont", "Lake Union", Some("w")),
    ("Friends of Discovery Park", "http://www.friendsdiscoverypark.org/", "website", Some("community"), Some(Set("park issues")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Friends of Fremont Peak Park", "http://www.fremontpeakpark.org/", "website", Some("community"), Some(Set("community organization")), "Fremont", "Lake Union", Some("w")),
    ("Friends of Frink Park", "http://www.frinkpark.org/", "website", Some("community"), Some(Set("park issues")), "Central District", "Central", Some("e")),
    ("Friends of Green Lake", "http://www.friendsofgreenlake.org/", "website", Some("community"), Some(Set("neighborhood group")), "Green Lake", "Northwest", Some("sw")),
    ("Friends of Seward Park", "http://www.sewardpark.org/", "website", Some("community"), Some(Set("park issues")), "Seward Park", "Southeast", Some("se")),
    ("Genesee-Schmitz Neighborhood Council", "http://genesee-schmitz.org/", "blog", Some("community"), Some(Set("neighborhood council")), "Genesee-Schmitz", "Southwest", Some("sw")),
    ("Georgetown Art Center", "http://georgetownartcenter.blogspot.com/", "blog", Some("community"), Some(Set("community arts")), "Georgetown", "Greater Duwamish", Some("s")),
    ("Georgetown Neighborhood", "http://www.georgetownneighborhood.com/", "website", Some("community"), Some(Set("community council")), "Georgetown", "Greater Duwamish", Some("s")),
    ("Georgetown Seattle", "http://groups.yahoo.com/group/Georgetown-Seattle/", "email_list", Some("community"), Some(Set("community members")), "Georgetown", "Greater Duwamish", Some("s")),
    ("Greenlake Community Council", "http://www.greenlakecommunitycouncil.org/", "website", Some("community"), Some(Set("community council")), "Green Lake", "Northwest", Some("sw")),
    ("Greenlake Community Wiki", "http://greenlake.wetpaint.com/", "wiki", Some("community"), Some(Set("events", "services", "for sale")), "Green Lake", "Northwest", Some("sw")),
    ("Greenwood Aurora Involved Neighbors", "http://www.gainseattle.com/", "website", Some("community"), Some(Set("neighborhood association")), "Greenwood", "Northwest", Some("sw")),
    ("Greenwood Blog", "http://www.greenwoodblog.com/", "blog", Some("commercial"), Some(Set("news", "events")), "Greenwood", "Northwest", Some("sw")),
    ("Greenwood Community Council", "http://www.greenwoodcommunitycouncil.org/about/", "website", Some("community"), Some(Set("community council")), "Greenwood", "Northwest", Some("sw")),
    ("Greenwood Community Council Announcements", "http://groups.yahoo.com/group/Greenwood_News/", "email_list", Some("community"), Some(Set("community council")), "Greenwood", "Northwest", Some("sw")),
    ("Greenwood Community Council Discussion", "http://groups.yahoo.com/group/greenwood-discussion/", "email_list", Some("community"), Some(Set("community council")), "Greenwood", "Northwest", Some("sw")),
    ("Greenwood Phinney Chamber of Commerce", "http://www.greenwood-phinney.com/", "website", Some("community"), Some(Set("chamber of commerce")), "Greenwood", "Northwest", Some("sw")),
    ("Haller Lake Community Club", "http://www.hallerlake.info/", "website", Some("community"), Some(Set("community organization")), "Haller Lake", "North", Some("n")),
    ("Hawthorne Hills Community Website", "http://www.seattle.gov/hawthornehills/", "website", Some("community"), Some(Set("community council")), "Hawthorne Hills", "Northeast", Some("ne")),
    ("Highland Park Action Committee", "http://www.highlandpk.net/", "website", Some("community"), Some(Set("community group")), "Highland Park", "Delridge", Some("sw")),
    ("Highland Park Improvement Club", "http://www.hpic1919.org/", "blog", Some("community"), Some(Set("neighborhood group")), "Highland Park", "Delridge", Some("sw")),
    ("InBallard", "http://inballard.com/", "website", Some("commercial"), Some(Set("shopping", "nightlife", "food", "services")), "Ballard", "Ballard", Some("nw")),
    ("Interbay District Blog", "http://interbayneighborhood.neighborlogs.com/", "blog", Some("community"), Some(Set("news", "sports")), "Interbay", "Magnolia/Queen Anne", Some("w")),
    ("Interbay Neighborhood Association", "http://www.our-interbay.org/", "website", Some("community"), Some(Set("neighborhood association")), "Interbay", "Magnolia/Queen Anne", Some("w")),
    ("Jefferson Park Alliance", "http://www.cityofseattle.net/commnty/Beacon/groups/jpa/", "website", Some("community"), Some(Set("community")), "Beacon Hill", "Greater Duwamish", Some("s")),
    ("Junction Neighborhood Organization", "http://www.wsjuno.com/", "blog", Some("community"), Some(Set("meetings", "events")), "Junction", "Southwest", Some("sw")),
    ("KOMO Communities - Ballard", "http://ballard.komonews.com/", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Ballard", "Ballard", Some("nw")),
    ("KOMO Communities - Beacon Hill", "http://beaconhill.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Beacon Hill", "Greater Duwamish", Some("s")),
    ("KOMO Communities - Captol Hill", "http://capitolhill.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Capitol Hill", "East", Some("e")),
    ("KOMO Communities - Central District", "http://centraldistrict.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Central District", "Central", Some("e")),
    ("KOMO Communities - Columbia City", "http://columbiacity.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Columbia City", "Southeast", Some("se")),
    ("KOMO Communities - Downtown", "http://downtown.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Downtown", "Downtown", Some("w")),
    ("KOMO Communities - Fremont", "http://fremont.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Fremont", "Lake Union", Some("w")),
    ("KOMO Communities - Georgetown", "http://georgetown.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Georgetown", "Greater Duwamish", Some("s")),
    ("KOMO Communities - Green Lake", "http://greenlake.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Green Lake", "Northwest", Some("sw")),
    ("KOMO Communities - Greenwood-Phinney", "http://greenwood-phinney.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Greenwood-Phinney", "Northwest", Some("sw")),
    ("KOMO Communities - Lake City", "http://lakecity.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Lake City", "North", Some("n")),
    ("KOMO Communities - Madison Park", "http://madisonpark.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Madison Park", "East", Some("e")),
    ("KOMO Communities - Magnolia", "http://magnolia.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("KOMO Communities - North Seattle", "http://northseattle.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "North Seattle", "North", Some("n")),
    ("KOMO Communities - Queen Anne", "http://queenanne.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Queen Anne", "Magnolia/Queen Anne", Some("w")),
    ("KOMO Communities - Rainier Valley", "http://rainiervalley.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "food", "criminal activity", "human interest", "news")), "Rainier Valley", "Southeast", Some("se")),
    ("KOMO Communities - South Lake Union", "http://southlakeunion.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "South Lake Union", "Lake Union", Some("w")),
    ("KOMO Communities - U-District", "http://udistrict.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "University District", "Northeast", Some("ne")),
    ("KOMO Communities - View Ridge", "http://viewridge.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "View Ridge", "Northeast", Some("ne")),
    ("KOMO Communities - Wallingford", "http://wallingford.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Wallingford", "Northwest", Some("sw")),
    ("KOMO Communities - West Seattle", "http://westseattle.komonews.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "West Seattle", "Southwest", Some("sw")),
    ("Lake City Live", "http://www.lakecitylive.net/", "blog", Some("commercial"), Some(Set("drink", "food", "reviews", "criminal activity", "news")), "Lake City", "North", Some("n")),
    ("Laurelhurst Community Club", "http://www.laurelhurstcc.com/", "website", Some("community"), Some(Set("community club")), "Laurelhurst", "Northeast", Some("ne")),
    ("Leschi Community Council", "http://groups.google.com/group/LeschiCC?hl=en", "email_list", Some("community"), Some(Set("meeting times", "planning")), "Leschi", "Central", Some("e")),
    ("Licton Springs Neighborhood ", "http://www.lictonsprings.org/", "website", Some("community"), Some(Set("neighborhood council")), "Licton Springs", "Northwest", Some("sw")),
    ("Longfellow Creek Community Website", "http://www.longfellowcreek.org/", "website", Some("community"), Some(Set("watershed info")), "Delridge", "Delridge", Some("sw")),
    ("MLK Business Association", "http://www.mlkba.org/", "website", Some("community"), Some(Set("business association")), "Rainier Valley", "Southeast", Some("se")),
    ("Madison Park Blogger", "http://madisonparkblogger.blogspot.com/", "blog", Some("commercial"), Some(Set("housing prices", "news", "events")), "Madison Park", "East", Some("e")),
    ("Madison Park Business Association", "http://www.madisonparkseattle.com/", "website", Some("community"), Some(Set("business association")), "Madison Park", "East", Some("e")),
    ("Madrona Moms", "http://health.groups.yahoo.com/group/MadronaMoms/", "email_list", Some("community"), Some(Set("community group (moms)")), "Madrona", "Central", Some("e")),
    ("Madrona Neighborhood", "http://madrona.wetpaint.com/", "wiki", Some("community"), Some(Set("etc.  Also community association meeting minutes", "favorite places", "neighborhood community site: buying/selling", "restaurants")), "Madrona", "Central", Some("e")),
    ("Magnolia Action Group", "http://www.orgsites.com/wa/mag/", "website", Some("community"), Some(Set("planning issues")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia Chamber of Commerce", "http://www.magnoliachamber.org/", "website", Some("community"), Some(Set("chamber of commerce")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia Historical Society", "http://www.magnoliahistoricalsociety.org/", "website", Some("community"), Some(Set("historical society")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia Neighborhood Planning Council", "http://magnolianpc.com/", "website", Some("community"), Some(Set("neighborhood association")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia Voice", "http://twitter.com/magnoliavoice", "twitter", Some("commercial"), Some(Set("criminal activity", "news", "events", "food")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia Voice", "http://www.facebook.com/pages/Magnolia-Voice-Seattle/116057104388", "facebook_page", Some("commercial"), Some(Set("criminal activity", "news", "events", "food")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia Voice", "http://www.magnoliavoice.com/", "blog", Some("commercial"), Some(Set("criminal activity", "news", "events", "food")), "Magnolia", "Magnolia/Queen Anne", Some("w")),
    ("Magnolia is Really Part of Seattle", "http://sleeplessinmagnolia.ning.com/", "wiki", Some("community"), Some(Set("planning issues")), "Magnolia", "Magnolia/Queen Anne", Some("w")),

    // Manually added (two missing values)
    ("Magnuson Community Garden", "http://cityofseattle.net/MAGNUSONGARDEN", "website", None, Some(Set("garden")), "Laurelhurst", "Northeast", None),

    ("Magnuson Environmental Stewardship Alliance", "http://mesaseattle.org/", "website", Some("community"), Some(Set("park issues")), "Laurelhurst", "Northeast", Some("ne")),
    ("Maple Leaf Community Council", "http://www.mapleleafcommunity.org/", "website", Some("community"), Some(Set("community council", "email list available")), "Maple Leaf", "Northeast", Some("ne")),
    ("Maple Leaf Life", "http://twitter.com/mapleleaflife", "twitter", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Maple Leaf", "Northeast", Some("ne")),
    ("Maple Leaf Life", "http://www.facebook.com/pages/Seattle-WA/Maple-Leaf-Life/298056021657", "facebook_page", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Maple Leaf", "Northeast", Some("ne")),
    ("Maple Leaf Life", "http://www.mapleleaflife.com/", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Maple Leaf", "Northeast", Some("ne")),
    ("Market On Wheels", "http://marketonwheels.wordpress.com/", "blog", Some("community"), Some(Set("farmers' market")), "South Park", "Greater Duwamish", Some("s")),
    ("Miller Park Neighborhood Association", "http://millerparkseattle.blogspot.com/", "blog", Some("community"), Some(Set("neighborhood association; news", "events")), "Miller Park", "East", Some("e")),
    ("Morgan Junction Community Association", "http://morganjunction.org/", "website", Some("community"), Some(Set("community association")), "Morgan Junction", "Southwest", Some("sw")),

    // Manually added (two missing values)
    ("Mount Baker Community Club", "http://groups.yahoo.com/group/MBCCCommunityNotices/", "email_list", None, Some(Set("community club")), "Mount Baker", "Southeast", None),

    ("Mount Baker Neighborhood ", "http://www.mountbaker.org/index.php", "website", Some("community"), Some(Set("community group")), "Mount Baker", "Southeast", Some("se")),
    ("My Greenlake Blog", "http://www.mygreenlake.com/", "blog", Some("commercial"), Some(Set("news", "events", "food")), "Green Lake", "Northwest", Some("sw")),
    ("MyBallard", "http://www.myballard.com/", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Ballard", "Ballard", Some("nw")),
    ("MyWallingford", "http://mywallingford.com", "blog", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Wallingford", "Northwest", Some("sw")),
    ("MyWallingford", "http://twitter.com/mywallingford", "twitter", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Wallingford", "Northwest", Some("sw")),
    ("MyWallingford", "http://www.facebook.com/MyWallingford?ref=ts", "facebook_page", Some("commercial"), Some(Set("shopping", "events", "criminal activity", "human interest", "news")), "Wallingford", "Northwest", Some("sw")),
    ("Nature Consortium", "http://www.naturec.org", "website", Some("community"), Some(Set("environmental conservation")), "Delridge", "Delridge", Some("sw")),
    ("ballardite blog", "http://www.ballardite.blogspot.com/", "blog", Some("personal"), Some(Set("personal", "news")), "Ballard", "Ballard", Some("nw")),
    ("belltown", "http://www.belltownpeople.com/", "blog", Some("commercial"), Some(Set("news", "events")), "Belltown", "Downtown", Some("w"))
  )


//    // Extractor
//    val dataFromFile = Community.name.url.`type`.orgtype.category.Neighborhood.name.District.name.region.hl.map { rec =>
//      rec.toList.map {
//        case set: Set[_] => set.map("\"" + _.toString + "\"")
//        case other       => "\"" + other.toString + "\""
//      }
//    }.map(e => e.mkString("\n(", ", ", ")"))
//    println(dataFromFile)
//    println()
//    println(seattleData.map(_._1).sorted.mkString("\n"))
}