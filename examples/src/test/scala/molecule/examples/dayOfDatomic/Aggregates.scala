package molecule.examples.dayOfDatomic
import molecule.api.out3._
import molecule.examples.dayOfDatomic.dsl.aggregates._
import molecule.examples.dayOfDatomic.schema.AggregatesSchema
import molecule.util.MoleculeSpec
import scala.language.postfixOps


class Aggregates extends MoleculeSpec {

  implicit val conn = recreateDbFrom(AggregatesSchema, "Aggregates")

  val planets  = Seq("Sun", "Jupiter", "Saturn", "Uranus", "Neptune", "Earth", "Venus", "Mars", "Ganymede", "Titan", "Mercury", "Callisto", "Io", "Moon", "Europa", "Triton", "Eris")
  val radiuses = Seq(696000.0, 69911.0, 58232.0, 25362.0, 24622.0, 6371.0, 6051.8, 3390.0, 2631.2, 2576.0, 2439.7, 2410.3, 1821.5, 1737.1, 1561.0, 1353.4, 1163.0)
  val url      = "http://en.wikipedia.org/wiki/List_of_Solar_System_objects_by_size"

  // Insert data with tx meta data
  Obj.name.meanRadius.Tx(Data.source_(url)) insert (planets zip radiuses)


  "Aggregated Attributes" >> {

    // Maximum value(s)
    Obj.meanRadius(max).get.head === 696000.0
    Obj.meanRadius(max(3)).get.head === List(696000.0, 69911.0, 58232.0)


    // Minimum value(s)
    Obj.meanRadius(min).get.head === 1163.0
    Obj.meanRadius(min(3)).get.head === List(1163.0, 1353.4, 1561.0)


    // Random value(s) - duplicates possible

    // Single random value
    val random = Obj.meanRadius(rand).get.head
    radiuses must contain(random)

    // 5 random values
    val randoms = Obj.meanRadius(rand(5)).get.head
    randoms.size === 5
    randoms.map(radiuses.contains(_)).reduce(_ && _) === true


    // Sample values - no duplicates

    // Single sample value
    val oneSample = Obj.meanRadius(sample).get.head
    radiuses must contain(oneSample)

    // 5 sample values
    val samples = Obj.meanRadius(sample(5)).get.head
    samples.size === 5
    samples.distinct.size === 5
    samples.map(radiuses.contains(_)).reduce(_ && _) === true


    println("Random:  " + random)
    println("Randoms: " + randoms)
    println("Sample:  " + oneSample)
    println("Samples: " + samples + "\n")
    ok
  }


  "Aggregate Calculations" >> {

    Obj.name(count).get.head === 17

    Obj.name(countDistinct).get.head === 17

    Obj.meanRadius(sum).get.head === 907633.0

    Obj.meanRadius(avg).get.head === 53390.17647058824

    Obj.meanRadius(median).get.head === 2631.2

    Obj.meanRadius(variance).get.head === 2.6212428511091213E10

    Obj.meanRadius(stddev).get.head === 161902.52780945456

    // We can even fetch multiple aggregates in one query:
    Obj.meanRadius(sum).meanRadius(avg).meanRadius(median).get.head === (907633.0, 53390.17647058824, 2631.2)
  }


  "Schema aggregations" >> {

    // What is the average length of a schema name?
    val attrs = Schema.a.get
    attrs.map(_.length).sum / attrs.size === 6

    // How many attributes does this schema use?
    Schema.ident.get.sorted === List(
      ":data/source",
      ":monster/heads",
      ":monster/name",
      ":obj/meanRadius",
      ":obj/name"
    )
    Schema.ident(count).get.head === 5

    // How many (Datomic) types does this schema use?
    Schema.tpe.get === List(
      "string", // String
      "double", // Double
      "long", // Int
    )
    Schema.tpe(count).get.head === 3
  }


  "Monsters" >> {

    Monster.name.heads insert List(
      ("Cerberus", 3),
      ("Medusa", 1),
      ("Cyclops", 1),
      ("Chimera", 1))

    Monster.heads(sum).get.head === 6 // 3 + 1 + 1 + 1

    // Other aggregations
    Monster.heads(avg).get.head === 1.5 // 6 / 4
    Monster.heads(median).get.head === 1
    Monster.heads(variance).get.head === 0.75 // ?
    Monster.heads(stddev).get.head === 0.8660254037844386 // ?


    // Add a twin Cyclop with 4 heads (not factual!)
    Monster.name.heads insert("Cyclops", 4)

    // Now we have a different set to aggregate over and compare
    Monster.name.heads.get === List(
      ("Cerberus", 3),
      ("Cyclops", 4),
      ("Chimera", 1),
      ("Cyclops", 1),
      ("Medusa", 1))

    Monster.heads(sum).get.head === 10 // 3 + 1 + 1 + 1 + 4
    Monster.heads(avg).get.head === 2.0 // 10 / 5
    Monster.heads(median).get.head === 1
    Monster.heads(variance).get.head === 1.6
    Monster.heads(stddev).get.head === 1.2649110640673518

    // Note how the query group by name so that we get 1 + 4 = 5 Cyclopes heads
    Monster.name.heads(sum).get === List(
      ("Cerberus", 3),
      ("Chimera", 1),
      ("Cyclops", 5), // <-- 1 + 4
      ("Medusa", 1))
  }
}