# Prepare running Molecule against all Datomic systems

Molecule transparently runs a unified Scala interface with only minor differences against the full matrix of Datomic systems/apis/languages which from a Molecule perspective boils down to 3 systems:

| System            | API                | Protocol | Language     | Download                   | License              |   
| :---:             | :---:              | :---:    | :---:        | :---:                      | :---:                |   
| peer              | datomic.api        | free     | Clojure/Java | [On-Prem free][free]       | Free | 
| peer-server       | datomic.client.api | dev      | Clojure      | [On-Prem starter/pro][pro] | Perpetual use, 1-year upgrades |   
| dev-local (cloud) | datomic.client.api | dev      | Clojure      | [Dev-tools][dev]           | Email reg            |   


Molecule is published with only a dependency on Datomic Free to be freely distributed. To run tests only against the free version, please follow the instructions in `README_free.md`

To run tests against the free and two proprietary systems too, please follow the steps below (some steps are only required once during setup).


## STEP 1 - Give Intellij enough memory (once)

In order for Intellij to be happy, set VM option -Xmx2G in preferences:

`Preferences > Build, Execution, Deployment > Compiler > Scala Compiler > Scala Compiler Server / VM options` 

Ensure that the java version (like 1.8) here is the same as in your terminal that you get from `java -version`


## STEP 2 - Download and install proprietary Datomic versions (once)

Register and install the two last proprietary Datomic systems from the links shown above into some folder locally.

Install libraries to your local maven repository (.m2): 

- Run `bin/maven-install` in datomic-pro-[version]
- Run `./install` in cognitect-dev-tools-[version]


## STEP 3 - Make proprietary-aware molecule available (once)

Open `project.SettingsDatomic` and set `datomicDownloadsDir` to the path of the directory where you downloaded the Datomic distributions.

In `project.Settings`, add "-SNAPSHOT" to the molecule version `version in ThisBuild := "<molecule-version>-SNAPSHOT"` and run `sbt publishLocal` from the molecule project directory. Let your projects use this proprietary-aware molecule version.

Molecule will automatically detect and default to using the latest available Datomic pro version if this is downloaded.

To enforce using the free version, you can compile molecule with this flag: 

`sbt compile -Dfree=true`

To enforce using a specific version, you can compile molecule with one or both of these flags:

`sbt compile -Ddatomic.pro=1.0.6202 -Ddatomic.dev-local=0.9.225`


## STEP 4 - Start transactor

Start Datomic transactor (update path/version number):

    cd <path-to-datomic-downloads>/datomic-pro-1.0.6222
    bin/transactor config/samples/dev-transactor-template.properties

While the transactor is running, create a new tab/process in the terminal to run the next steps:


## STEP 5 - Install sample databases (once)

Run commands below to perform these 3 operations:
- Install `datomic-samples` for dev-local
- Download and unpack mbrainz-1968-1973 special sample for peer and peer-server
- Restore mbrainz sample


    wget https://datomic-samples.s3.amazonaws.com/datomic-samples-2020-07-07.zip
    unzip datomic-samples-2020-07-07.zip 
    rm datomic-samples-2020-07-07.zip
    wget https://s3.amazonaws.com/mbrainz/datomic-mbrainz-1968-1973-backup-2017-07-20.tar -O mbrainz.tar 
    tar -xvf mbrainz.tar -C datomic-samples 
    rm mbrainz.tar 
    bin/datomic restore-db file://`pwd`/datomic-samples/mbrainz-1968-1973 datomic:dev://localhost:4334/mbrainz-1968-1973


## STEP 6 - Create molecule samples (once)

Run `molecule.setup.RecreateTestDbs` in molecule-tests to create sample databases. This is necessary before starting the peer server so that it can "see" the mbrainz-subset db.


## STEP 7 - Start peer server
                           
Copy all lines below and paste into terminal to start the Peer Server and having it serve all the sample databases used by the tests:

    bin/run -m datomic.peer-server -h localhost -p 8998 -a k,s \
    -d m_txCount,datomic:mem://m_txCount \
    -d m_coretests,datomic:mem://m_coretests \
    -d m_bidirectional,datomic:mem://m_bidirectional \
    -d m_partitions,datomic:mem://m_partitions \
    -d m_nested,datomic:mem://m_nested \
    -d m_selfjoin,datomic:mem://m_selfjoin \
    -d m_aggregates,datomic:mem://m_aggregates \
    -d m_socialNews,datomic:mem://m_socialNews \
    -d m_graph,datomic:mem://m_graph \
    -d m_graph2,datomic:mem://m_graph2 \
    -d m_modernGraph1,datomic:mem://m_modernGraph1 \
    -d m_modernGraph2,datomic:mem://m_modernGraph2 \
    -d m_productsOrder,datomic:mem://m_productsOrder \
    -d m_seattle,datomic:mem://m_seattle \
    -d mbrainz-1968-1973,datomic:dev://localhost:4334/mbrainz-1968-1973 \
    -d mbrainz-subset,datomic:dev://localhost:4334/mbrainz-subset

Now you can run molecule tests or projects against peer, peer-server and dev-local (cloud).

## Test
Test in IDE or with sbt. Observe that first time tests are run, all test files have to be compiled which can take a few minutes.
```
sbt

// Test (against scala 2.13 as default)
sbt:molecule> testOnly molecule.tests.*
sbt:molecule> testOnly molecule.tests.core.ref.*
sbt:molecule> testOnly molecule.tests.core.ref.TwoStepQueries

// Test against scala 2.13 explicitly
sbt:molecule> ++2.13.4; testOnly molecule.tests.*
sbt:molecule> ++2.13.4; testOnly molecule.tests.core.ref.*
sbt:molecule> ++2.13.4; testOnly molecule.tests.core.ref.TwoStepQueries

// Test against scala 2.12 
sbt:molecule> ++2.12.12; testOnly molecule.tests.*
sbt:molecule> ++2.12.12; testOnly molecule.tests.core.ref.*
sbt:molecule> ++2.12.12; testOnly molecule.tests.core.ref.TwoStepQueries
```
Using sbt is about twice as fast and therefore preferable when running all tests. Remember to ctrl-c the sbt process when switching to test in IDE to avoid process locks.

## Further info

Info about the mbrainz sample data:

- http://blog.datomic.com/2013/07/datomic-musicbrainz-sample-database.html
- https://github.com/Datomic/mbrainz-sample
- https://github.com/Datomic/mbrainz-sample/wiki/Queries

            
[free]:https://my.datomic.com/downloads/free
[pro]:https://www.datomic.com/get-datomic.html
[dev]:https://cognitect.com/dev-tools