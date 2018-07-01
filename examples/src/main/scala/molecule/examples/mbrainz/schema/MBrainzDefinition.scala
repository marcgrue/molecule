package molecule.examples.mbrainz.schema
import molecule.schema.definition._

@InOut(3, 8)
object MBrainzDefinition {

  trait AbstractRelease {
    val name         = oneString
    val artistCredit = oneString.fulltextSearch
    val gid          = oneUUID.uniqueIdentity
    val `type`       = oneEnum('album, 'single, 'ep, 'audiobook, 'other)
    val artists      = many[Artist]
  }

  trait Artist {
    val startYear  = oneLong
    val startMonth = oneLong
    val startDay   = oneLong
    val endYear    = oneLong
    val endMonth   = oneLong
    val endDay     = oneLong
    val sortName   = oneString
    val name       = oneString.fulltextSearch
    val gid        = oneUUID.uniqueIdentity
    val `type`     = oneEnum('person, 'group, 'other)
    val gender     = oneEnum('male, 'female, 'other)
    val country    = one[Country]
//    val country    = one[Medium]
  }

  trait Country {
    val name = oneString.uniqueValue
  }

//  trait Label {
//    val startYear  = oneLong
//    val startMonth = oneLong
//    val startDay   = oneLong
//    val endYear    = oneLong
//    val endMonth   = oneLong
//    val endDay     = oneLong
//    val sortName   = oneString
//    val name       = oneString.fulltextSearch
//    val gid        = oneUUID.uniqueIdentity
//    val `type`     = oneEnum('distributor, 'holding, 'production, 'originalProduction, 'bootlegProduction, 'reissueProduction, 'publisher)
//    val country    = one[Country]
//  }
//
//  trait Language {
//    val name = oneString.uniqueValue
//  }

  trait Medium {
    val name       = oneString.fulltextSearch
    val position   = oneLong
//    val position   = manyLong
    val trackCount = oneLong
    val format     = oneEnum('dvdVideo, 'laserDisc, 'cd, 'hddvd, 'vhs, 'svcd, 'dcc, 'cdr, 'slotMusic, 'bluray, 'waxCylinder, 'cartridge, 'umd, 'miniDisc, 'vinyl, 'vinyl12, 'sacd, 'other, 'dualDisc, 'vinyl10, 'dvd, 'pianoRoll, 'betamax, 'vcd, 'dat, 'reel, 'vinyl7, 'dvdAudio, 'digitalMedia, 'hdcd, 'videotape, 'usbFlashDrive, 'cassette, 'cd8cm)
    val tracks     = many[Track].isComponent
  }

  trait Release {
    val year            = oneLong
    val month           = oneLong
    val day             = oneLong
    val artistCredit    = oneString.fulltextSearch
    val status          = oneString
    val barcode         = oneString
    val name            = oneString.fulltextSearch
    val gid             = oneUUID.uniqueIdentity
    val artists         = many[Artist]
    val abstractRelease = one[AbstractRelease]
//    val language        = one[Language]
    val media           = many[Medium].isComponent
    val packaging       = oneEnum('jewelCase, 'slimJewelCase, 'digipak, 'none, 'keepCase, 'cardboardPaperSleeve, 'other)
//    val script          = one[Script]
//    val label           = one[Label]
    val country         = one[Country]
  }

//  trait Script {
//    val name = oneString.uniqueValue
//  }

  trait Track {
    val position     = oneLong
//    val positionX     = oneLong
//    val position     = manyString
//    val position     = oneString

    val duration     = oneLong
//    val artistCredit = oneString.fulltextSearch
    val name         = oneString.fulltextSearch
    val artists      = many[Artist]
//    val artists      = many[Release]
  }
}