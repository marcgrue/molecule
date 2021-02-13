package molecule.tests.core.schemaDef

import molecule.core.ast.elements.{Atom, Bond, Eq, Model}
import molecule.tests.core.schemaDef.dsl.PartitionTest._
import molecule.datomic.api.out4._
import molecule.setup.TestSpec
import scala.language.reflectiveCalls


class PartitionTests extends TestSpec {

  "Save resolves to correct partitions" in new PartitionSetup {

    lit_Book.title("A good book").cat("good").Author.name("Marc").gender("male").inspectSave

    /*
## 1 ## output.Molecule.inspectSave
================================================================================================================
Model(
  Atom("lit_Book", "title", "String", 1, Eq(Seq("A good book")), None, Seq(), Seq())
  Atom("lit_Book", "cat", "String", 1, Eq(Seq("good")), Some(":lit_Book.cat/"), Seq(), Seq())
  Bond("lit_Book", "author", "gen_Person", 1, Seq())
  Atom("gen_Person", "name", "String", 1, Eq(Seq("Marc")), None, Seq(), Seq())
  Atom("gen_Person", "gender", "String", 1, Eq(Seq("male")), Some(":gen_Person.gender/"), Seq(), Seq()))
----------------------------------------------------------------------------------------------------------------
List(
  List(":db/add",     __tempId, ":lit_Book/title",            Values(Eq(Seq("A good book")),None), Card(1)),
  List(":db/add",     e       , ":lit_Book/cat",              Values(Eq(Seq("good")),Some(:lit_Book.cat/)), Card(1)),
  list(":db/add",     e       , ":lit_Book/author",           :gen_Person),
  List(":db/add",     v       , ":gen_Person/name",           Values(Eq(Seq("Marc")),None), Card(1)),
  List(":db/add",     e       , ":gen_Person/gender",         Values(Eq(Seq("male")),Some(:gen_Person.gender/)), Card(1)))
----------------------------------------------------------------------------------------------------------------
list(
  list(":db/add",     #db/id[:lit -1000025], ":lit_Book/title",            A good book),
  list(":db/add",     #db/id[:lit -1000025], ":lit_Book/cat",              :lit_Book.cat/good),
  list(":db/add",     #db/id[:lit -1000025], ":lit_Book/author",           #db/id[:gen -1000026]),
  list(":db/add",     #db/id[:gen -1000026], ":gen_Person/name",           Marc),
  list(":db/add",     #db/id[:gen -1000026], ":gen_Person/gender",         :gen_Person.gender/male))
================================================================================================================
     */
  }


  "Nested 2 levels" in new PartitionSetup {
    m(lit_Book.title.Reviewers.name.Professions * gen_Profession.name) insert List(("book", "Jan", List("Musician")))

    m(lit_Book.title.Reviewers.name.Professions * gen_Profession.name).get === List(("book", "Jan", List("Musician")))
    // Same as
    m(lit_Book.title.Reviewers.Professions * gen_Profession.name).get === List(("book", List("Musician")))
  }


  "Nested 2 levels without intermediary attribute values" in new PartitionSetup {
    m(lit_Book.title.Reviewers.Professions * gen_Profession.name) insert List(("book", List("Hacker", "Magician")))

    m(lit_Book.title.Reviewers * gen_Person.Professions.name).get.map(r => (r._1, r._2.sorted)) ===
      List(("book", List("Hacker", "Magician")))
    // Same as
    m(lit_Book.title.Reviewers.Professions * gen_Profession.name).get.map(r => (r._1, r._2.sorted)) ===
      List(("book", List("Hacker", "Magician")))
  }


  "Nested 2 levels + many reference" in new PartitionSetup {
    m(lit_Book.title.Reviewers * gen_Person.Professions.name) insert List(("book", List("Hacker", "Magician")))

    m(lit_Book.title.Reviewers * gen_Person.Professions.name).get === List(("book", List("Hacker", "Magician")))
    // Same as
    m(lit_Book.title.Reviewers.Professions * gen_Profession.name).get === List(("book", List("Hacker", "Magician")))
  }


  "Back ref" >> {

    "Back only" in new PartitionSetup {
      lit_Book.title("A good book").cat("good").Author.name("Marc").save
      lit_Book.title.Author.name._lit_Book.cat.get.head === ("A good book", "Marc", "good")
    }

    "Adjacent" in new PartitionSetup {
      m(lit_Book.title.Author.name._lit_Book.Reviewers.name) insert List(("book", "John", "Marc"))

      m(lit_Book.title.Author.name._lit_Book.Reviewers.name).get === List(("book", "John", "Marc"))
      m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name).get === List(("book", "John", List("Marc")))
    }

    "Nested" in new PartitionSetup {
      m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name) insert List(("book", "John", List("Marc")))

      m(lit_Book.title.Author.name._lit_Book.Reviewers.name).get === List(("book", "John", "Marc"))
      m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name).get === List(("book", "John", List("Marc")))
    }

    "Nested + adjacent" in new PartitionSetup {
      m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name.Professions.name) insert List(("book", "John", List(("Marc", "Musician"))))

      m(lit_Book.title.Author.name._lit_Book.Reviewers.name.Professions.name).get === List(("book", "John", "Marc", "Musician"))
      m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name.Professions.name).get === List(("book", "John", List(("Marc", "Musician"))))
    }

    "Nested + nested" in new PartitionSetup {
      m(lit_Book.title.Author.name._lit_Book.Reviewers * (gen_Person.name.Professions * gen_Profession.name)) insert List(("book", "John", List(("Marc", List("Musician")))))

      m(lit_Book.title.Author.name._lit_Book.Reviewers.name.Professions.name).get === List(("book", "John", "Marc", "Musician"))
      m(lit_Book.title.Author.name._lit_Book.Reviewers * (gen_Person.name.Professions * gen_Profession.name)).get === List(("book", "John", List(("Marc", List("Musician")))))
    }
  }
}