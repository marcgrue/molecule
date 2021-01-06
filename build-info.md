

Compile to 2.13 only:

    sbt clean compile

Compile to 2.12 only:

    sbt ++2.12.12 clean compile

Compile to 2.12 and 2.13:

    sbt clean +compile









> sbt
> moleculeJS/fastOptJS



Publishing instructions:
OBS: Check that changes applied in jvm module are applied to both 2.12 and 2.13!
// 2.12 & 2.13:
> sbt +moleculeJVM/publishLocal +moleculeJS/publishLocal -Dfree=true
or
> sbt +moleculeJVM/publishSigned +moleculeJS/publishSigned -Dfree=true

// 2.13 only
> sbt ++2.13.4 moleculeJVM/publishLocal moleculeJS/publishLocal

Delete previous ivy cached build files before publishing locally (if not using SNAPSHOT version)
> del ~/.ivy2/local/org.scalamolecule/molecule*