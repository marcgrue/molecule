package molecule.coretests.perf

import molecule.imports._
import molecule.util.MoleculeSpec
import molecule.coretests.util.schema.CoreTestSchema

class CompilationPerf extends MoleculeSpec {

  // Simple (un-scientific) compilation time tests with increasing arity molecules.


  implicit val conn = recreateDbFrom(CoreTestSchema)
  /*
  No molecules

  s  ms

  1 438
  1 473
  1 292
  1 282
  1 321
  1 308
  1 405
  1 455
  1 345
  1 343
  */

  //  m(Ns.str)
  /*
  Arity 1

  2 549
  2 568
  2 576
  2 650
  2 584
  2 793
  2 602
  2 625
  2 513
  2 577
  */

  //  m(Ns.str.int)
  /*
  Arity 2

  2 870
  2 742
  2 625
  2 870
  2 625
  2 698
  2 770
  2 589
  3 045
  2 764
  */

  //  m(Ns.str.int.long)
  /*
  Arity 3

  2 761
  2 827
  2 717
  2 686
  2 833
  2 742
  2 914
  2 655
  2 714
  2 604
  */

  //  m(Ns.str.int.long.float)
  /*
  Arity 4

  2 991
  2 934
  2 899
  3 183
  3 000
  2 839
  2 891
  3 019
  3 352
  2 745
  */

  //  m(Ns.str.int.long.float.double)
  /*
  Arity 5

  3 350
  3 733
  3 650
  3 166
  3 181
  3 208
  3 093
  2 678
  2 467
  2 623
  2 681
  */

  //  m(Ns.str.int.long.float.double.bool)
  /*
  Arity 6

  2 826
  2 686
  2 739
  2 764
  3 264
  2 857
  2 757
  2 886
  2 913
  2 880
  */

  //  m(Ns.str.int.long.float.double.bool.date)
  /*
  Arity 7

3 006
2 794
2 792
3 042
2 985
3 536
2 964
2 976
3 201
3 333
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid)
  /*
  Arity 8

3 330
3 036
3 017
2 982
2 991
4 023
2 935
3 701
3 477
3 179
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri)
  /*
  Arity 9

3 627
3 390
3 704
4 184
3 716
3 295
3 882
3 701
3 683
3 657
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum)
  /*
  Arity 10

3 698
3 652
3 628
3 816
3 440
3 726
3 711
3 252
3 431
3 327
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs)
  /*
  Arity 11

4 552
4 323
4 423
4 479
4 505
4 425
4 498
4 344
4 329
4 279
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints)
  /*
  Arity 12

5 614
5 508
6 063
5 614
4 563
4 608
4 604
4 715
4 576
4 651
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs)
  /*
  Arity 13

6 772
7 187
7 124
7 325
8 084
7 654
7 466
7 866
7 710
7 476
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats)
  /*
  Arity 14

12 233
11 910
12 219
11 894
12 090

15 855
10 537
11 529
10 098
10 710
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles)
  /*
  Arity 15

19 967
17 376
18 017
17 693
16 831
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools)
  /*
  Arity 16

29 798

  */
  // Same molecule as above split into a composite
  //  m(Ns.str.int.long.float.double.bool.date.uuid ~ Ns.uri.enum.strs.ints.longs.floats.doubles.bools)
  /*
  Composite arity 8 + 8 makes compilation fast again

3 308
3 614
3 532
3 460
3 886

  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools.dates)
  /*
  Arity 17

55 987
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools.dates.uuids)
  /*
  Arity 18

101 013  -  1m 41s 13ms
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools.dates.uuids.uris)
  /*
  Arity 19
224 098  -  3m 44s 98ms
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums)
  /*
  Arity 20
278 547  -  4m 38 547
  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum ~ Ns.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums)
  /*
  Arity 10 + 10

10 306
7 819
7 635
7 273
  */

  //  m(Ns.str.int.long.float.double.bool.date ~ Ns.uuid.uri.enum.strs.ints.longs.floats ~ Ns.doubles.bools.dates.uuids.uris.enums)
  /*
  Arity 7 + 7 + 6   - even faster!

5 284
5 472
5 113

  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums.ref1)
  /*
  Arity 21

  ???

  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums.ref1.refs1)
  /*
  Arity 22

  ???

  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.strs ~ Ns.ints.longs.floats.doubles.bools.dates.uuids.uris.enums.ref1.refs1)
  /*
  Arity 11 + 11

11 960
8 711
11 706
8 985
8 662

  */

  //  m(Ns.str.int.long.float.double.bool.date.uuid ~ Ns.uri.enum.strs.ints.longs.floats.doubles ~ Ns.bools.dates.uuids.uris.enums.ref1.refs1)
  /*
  Arity 8 + 7 + 7

8 259
7 521
7 885
7 928
8 350

  */
}
