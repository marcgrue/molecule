
> sbt
> moleculeJS/fastOptJS



Publishing instructions:
OBS: Check that changes applied in jvm module are applied to both 2.12 and 2.13!
// 2.12 & 2.13:
> sbt +moleculeJVM/publishLocal +moleculeJS/publishLocal
or
> sbt +moleculeJVM/publishSigned +moleculeJS/publishSigned

// 2.13 only
> sbt ++2.13.2 moleculeJVM/publishLocal moleculeJS/publishLocal

Delete previous ivy cached build files before publishing locally (if not using SNAPSHOT version)
> del ~/.ivy2/local/org.scalamolecule/molecule*