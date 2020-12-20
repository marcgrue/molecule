# Prepare running Molecule against Datomic Free

Molecule transparently runs a unified Scala interface with only minor differences 
against the full matrix of Datomic systems/apis/languages which from a Molecule 
perspective boils down to 3 systems:

| System            | API                | Protocol | Language     | Download                   | License              |   
| :---:             | :---:              | :---:    | :---:        | :---:                      | :---:                |   
| peer              | datomic.api        | free     | Clojure/Java | [On-Prem free][free]       | Datomic Free Edition | 
| peer-server       | datomic.client.api | dev      | Clojure      | [On-Prem starter/pro][pro] | EULA                 |   
| dev-local (cloud) | datomic.client.api | dev      | Clojure      | [Dev-tools][dev]           | Email reg            |   


Molecule is published with only a dependency on Datomic Free to be freely distributed. 

To run tests against the two proprietary systems listed above
too, please follow the instructions in `README_pro.md` 

Run test against only the free version by completing the following steps:


## STEP 1 - Give Intellij enough memory (once)

In order for Intellij to be happy, set VM option -Xmx2G in preferences:
`Preferences > Build, Execution, Deployment > Compiler > Scala Compiler > Scala Compiler Server / VM options`

Ensure that the java version (like 1.8) here is the same as in your terminal that you get from `java -version`

## STEP 2 - Set datomic directory path (once)

Open `project.SettingsDatomic` and set `datomicDownloadsDir` to the path of the
directory where you downloaded the Datomic distributions.

Molecule will automatically detect and default to using the latest available 
Datomic pro version if this is downloaded. To enforce using the free version, 
you can compile molecule with this flag: `sbt compile -Dfree=true`


## STEP 3 - Start transactor

Start Datomic transactor (update path/version number):

    cd /Users/mg/lib/datomic/datomic-free-0.9.5697
    bin/transactor config/samples/dev-transactor-template.properties

While the transactor is running, create a new tab/process in the terminal to run the next steps:


## STEP 4 - Install mbrainz-1968-1973 sample database (once)

    wget https://s3.amazonaws.com/mbrainz/datomic-mbrainz-1968-1973-backup-2017-07-20.tar -O mbrainz.tar 
    tar -xvf mbrainz.tar -C samples 
    rm mbrainz.tar 
    bin/datomic restore-db file://`pwd`/samples/mbrainz-1968-1973 datomic:free://localhost:4334/mbrainz-1968-1973

Now you can run molecule tests or projects against Datomic free.

## Test
Test in IDE or with sbt:
```
sbt

// Single test
sbt:molecule> testOnly molecule.tests.core.ref.TwoStepQueries

// Group of tests (works only with peer, so set `tests` to 1 in TestSpec)
sbt:molecule> testOnly molecule.tests.core.ref.*

// All tests (works only with peer, so set `tests` to 1 in TestSpec)
sbt:molecule> test
```
Using sbt is about twice as fast and therefore preferable
when running all tests. Remember to ctrl-c the sbt process when switching to
test in IDE to avoid process locks.


## Further info

Info about the mbrainz sample data:

- http://blog.datomic.com/2013/07/datomic-musicbrainz-sample-database.html
- https://github.com/Datomic/mbrainz-sample
- https://github.com/Datomic/mbrainz-sample/wiki/Queries

            
[free]:https://my.datomic.com/downloads/free
[pro]:https://www.datomic.com/get-datomic.html
[dev]:https://cognitect.com/dev-tools