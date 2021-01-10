# Build cmds cheat sheet


## Compile

Compile to 2.13 only:

    sbt clean compile

Compile to 2.12 only:

    sbt ++2.12.12 clean compile

Compile to 2.12 and 2.13:

    sbt clean +compile


## Compile JS

> sbt
> moleculeJS/fastOptJS


## Publishing

- Check that changes applied in jvm module are applied to both 2.12 and 2.13!
- Un-comment `Publish.withDocs` in build.sbt

2.12 & 2.13:
> sbt +publishLocal -Dfree=true

or
 
> sbt +publishSigned -Dfree=true


// 2.13 only
> sbt publishLocal

// 2.12 only
> sbt ++2.12.12 publishSigned

