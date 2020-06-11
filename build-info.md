
> sbt
> moleculeCoreJS/fastOptJS



Publishing instructions:
// 2.12 & 2.13:
> sbt +moleculeCoreJVM/publishLocal +moleculeCoreJS/publishLocal
or
> sbt +moleculeCoreJVM/publishSigned +moleculeCoreJS/publishSigned

// 2.13 only
> sbt ++2.13.2 moleculeCoreJVM/publishLocal moleculeCoreJS/publishLocal

Delete previous ivy cached build files before publishing locally (if not using SNAPSHOT version)
> del ~/.ivy2/local/org.scalamolecule/molecule*