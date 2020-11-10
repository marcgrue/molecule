package molecule.core.composition

import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.out._
import scala.language.higherKinds

/** Transaction meta data on molecule.
  * <br><br>
  * `Tx` takes a transaction meta data molecule with attributes having the transaction id as their entity id.
  * {{{
  *   // Save molecule with transaction data
  *   Person.name("Ben").Tx(MyMetaData.action("add member")).save.eid
  *
  *   // Query for data with transaction meta data - "which persons became members"
  *   Person.name.Tx(MyMetaData.action_("add member")).get === List("Ben")
  * }}}
  * @see Manual: [[http://www.scalamolecule.org/manual/transactions/tx-meta-data/ Manual]]
  *      | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/transaction/TransactionMetaData.scala#L1 Tests]]
  */
trait Tx

/** `Tx` interface to save and query tx meta data. */
object Tx {

  trait Tx00 extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_0[P0, P1, P1, P2                                                                       ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_1[P1, P2, P2, P3, a                                                                    ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_2[P2, P3, P3, P4, a, b                                                                 ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_3[P3, P4, P4, P5, a, b, c                                                              ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_4[P4, P5, P5, P6, a, b, c, d                                                           ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_5[P5, P6, P6, P7, a, b, c, d, e                                                        ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_6[P6, P7, P7, P8, a, b, c, d, e, f                                                     ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_7[P7, P8, P8, P9, a, b, c, d, e, f, g                                                  ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_8[P8, P9, P9, P10, a, b, c, d, e, f, g, h                                              ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_9[P9, P10, P10, P11, a, b, c, d, e, f, g, h, i                                         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_10[P10, P11, P11, P12, a, b, c, d, e, f, g, h, i, j                                    ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_11[P11, P12, P12, P13, a, b, c, d, e, f, g, h, i, j, k                                 ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_12[P12, P13, P13, P14, a, b, c, d, e, f, g, h, i, j, k, l                              ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_13[P13, P14, P14, P15, a, b, c, d, e, f, g, h, i, j, k, l, m                           ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_14[P14, P15, P15, P16, a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_15[P15, P16, P16, P17, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_16[P16, P17, P17, P18, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] (txMetaData: NS17[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ]): Out_17[P17, P18, P18, P19, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ] (txMetaData: NS18[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ]): Out_18[P18, P19, P19, P20, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ] (txMetaData: NS19[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ]): Out_19[P19, P20, P20, P21, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ] (txMetaData: NS20[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ]): Out_20[P20, P21, P21, P22, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u   ] (txMetaData: NS21[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u   ]): Out_21[P21, P22, P22, P23, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v] (txMetaData: NS22[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v]): Out_22[P22, P23, P23, P24, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v] = ???
    }
  }

  trait Tx01[A] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_1[P1, P2, P2, P3, A                                                                    ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_2[P2, P3, P3, P4, A, a                                                                 ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_3[P3, P4, P4, P5, A, a, b                                                              ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_4[P4, P5, P5, P6, A, a, b, c                                                           ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_5[P5, P6, P6, P7, A, a, b, c, d                                                        ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_6[P6, P7, P7, P8, A, a, b, c, d, e                                                     ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_7[P7, P8, P8, P9, A, a, b, c, d, e, f                                                  ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_8[P8, P9, P9, P10, A, a, b, c, d, e, f, g                                              ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_9[P9, P10, P10, P11, A, a, b, c, d, e, f, g, h                                         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_10[P10, P11, P11, P12, A, a, b, c, d, e, f, g, h, i                                    ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_11[P11, P12, P12, P13, A, a, b, c, d, e, f, g, h, i, j                                 ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_12[P12, P13, P13, P14, A, a, b, c, d, e, f, g, h, i, j, k                              ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_13[P13, P14, P14, P15, A, a, b, c, d, e, f, g, h, i, j, k, l                           ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_14[P14, P15, P15, P16, A, a, b, c, d, e, f, g, h, i, j, k, l, m                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_15[P15, P16, P16, P17, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_16[P16, P17, P17, P18, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_17[P17, P18, P18, P19, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] (txMetaData: NS17[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ]): Out_18[P18, P19, P19, P20, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ] (txMetaData: NS18[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ]): Out_19[P19, P20, P20, P21, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ] (txMetaData: NS19[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ]): Out_20[P20, P21, P21, P22, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ] (txMetaData: NS20[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ]): Out_21[P21, P22, P22, P23, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u   ] (txMetaData: NS21[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u   ]): Out_22[P22, P23, P23, P24, A, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u] = ???
    }
  }

  trait Tx02[A, B] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_2[P2, P3, P3, P4, A, B                                                                 ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_3[P3, P4, P4, P5, A, B, a                                                              ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_4[P4, P5, P5, P6, A, B, a, b                                                           ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_5[P5, P6, P6, P7, A, B, a, b, c                                                        ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_6[P6, P7, P7, P8, A, B, a, b, c, d                                                     ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_7[P7, P8, P8, P9, A, B, a, b, c, d, e                                                  ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_8[P8, P9, P9, P10, A, B, a, b, c, d, e, f                                              ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_9[P9, P10, P10, P11, A, B, a, b, c, d, e, f, g                                         ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_10[P10, P11, P11, P12, A, B, a, b, c, d, e, f, g, h                                    ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_11[P11, P12, P12, P13, A, B, a, b, c, d, e, f, g, h, i                                 ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_12[P12, P13, P13, P14, A, B, a, b, c, d, e, f, g, h, i, j                              ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_13[P13, P14, P14, P15, A, B, a, b, c, d, e, f, g, h, i, j, k                           ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_14[P14, P15, P15, P16, A, B, a, b, c, d, e, f, g, h, i, j, k, l                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_15[P15, P16, P16, P17, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_16[P16, P17, P17, P18, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_17[P17, P18, P18, P19, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_18[P18, P19, P19, P20, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] (txMetaData: NS17[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ]): Out_19[P19, P20, P20, P21, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ] (txMetaData: NS18[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ]): Out_20[P20, P21, P21, P22, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ] (txMetaData: NS19[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ]): Out_21[P21, P22, P22, P23, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ] (txMetaData: NS20[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t      ]): Out_22[P22, P23, P23, P24, A, B, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t] = ???
    }
  }

  trait Tx03[A, B, C] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_3[P3, P4, P4, P5, A, B, C                                                              ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_4[P4, P5, P5, P6, A, B, C, a                                                           ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_5[P5, P6, P6, P7, A, B, C, a, b                                                        ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_6[P6, P7, P7, P8, A, B, C, a, b, c                                                     ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_7[P7, P8, P8, P9, A, B, C, a, b, c, d                                                  ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_8[P8, P9, P9, P10, A, B, C, a, b, c, d, e                                              ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_9[P9, P10, P10, P11, A, B, C, a, b, c, d, e, f                                         ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_10[P10, P11, P11, P12, A, B, C, a, b, c, d, e, f, g                                    ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_11[P11, P12, P12, P13, A, B, C, a, b, c, d, e, f, g, h                                 ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_12[P12, P13, P13, P14, A, B, C, a, b, c, d, e, f, g, h, i                              ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_13[P13, P14, P14, P15, A, B, C, a, b, c, d, e, f, g, h, i, j                           ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_14[P14, P15, P15, P16, A, B, C, a, b, c, d, e, f, g, h, i, j, k                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_15[P15, P16, P16, P17, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_16[P16, P17, P17, P18, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_17[P17, P18, P18, P19, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m, n               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_18[P18, P19, P19, P20, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_19[P19, P20, P20, P21, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] (txMetaData: NS17[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ]): Out_20[P20, P21, P21, P22, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ] (txMetaData: NS18[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ]): Out_21[P21, P22, P22, P23, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ] (txMetaData: NS19[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s         ]): Out_22[P22, P23, P23, P24, A, B, C, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s] = ???
    }
  }

  trait Tx04[A, B, C, D] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_4[P4, P5, P5, P6, A, B, C, D                                                          ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_5[P5, P6, P6, P7, A, B, C, D, a                                                       ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_6[P6, P7, P7, P8, A, B, C, D, a, b                                                    ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_7[P7, P8, P8, P9, A, B, C, D, a, b, c                                                 ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_8[P8, P9, P9, P10, A, B, C, D, a, b, c, d                                             ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_9[P9, P10, P10, P11, A, B, C, D, a, b, c, d, e                                        ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_10[P10, P11, P11, P12, A, B, C, D, a, b, c, d, e, f                                    ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_11[P11, P12, P12, P13, A, B, C, D, a, b, c, d, e, f, g                                 ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_12[P12, P13, P13, P14, A, B, C, D, a, b, c, d, e, f, g, h                              ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_13[P13, P14, P14, P15, A, B, C, D, a, b, c, d, e, f, g, h, i                           ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_14[P14, P15, P15, P16, A, B, C, D, a, b, c, d, e, f, g, h, i, j                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_15[P15, P16, P16, P17, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_16[P16, P17, P17, P18, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_17[P17, P18, P18, P19, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l, m               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_18[P18, P19, P19, P20, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l, m, n            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_19[P19, P20, P20, P21, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_20[P20, P21, P21, P22, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] (txMetaData: NS17[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ]): Out_21[P21, P22, P22, P23, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ] (txMetaData: NS18[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r            ]): Out_22[P22, P23, P23, P24, A, B, C, D, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r] = ???
    }
  }

  trait Tx05[A, B, C, D, E] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_5[P5, P6, P6, P7, A, B, C, D, E                                                       ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_6[P6, P7, P7, P8, A, B, C, D, E, a                                                    ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_7[P7, P8, P8, P9, A, B, C, D, E, a, b                                                 ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_8[P8, P9, P9, P10, A, B, C, D, E, a, b, c                                             ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_9[P9, P10, P10, P11, A, B, C, D, E, a, b, c, d                                        ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_10[P10, P11, P11, P12, A, B, C, D, E, a, b, c, d, e                                    ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, a, b, c, d, e, f                                 ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, a, b, c, d, e, f, g                              ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, a, b, c, d, e, f, g, h                           ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, a, b, c, d, e, f, g, h, i                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l, m            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l, m, n         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ] (txMetaData: NS17[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q               ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q] = ???
    }
  }

  trait Tx06[A, B, C, D, E, F] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_6[P6, P7, P7, P8, A, B, C, D, E, F                                                    ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_7[P7, P8, P8, P9, A, B, C, D, E, F, a                                                 ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_8[P8, P9, P9, P10, A, B, C, D, E, F, a, b                                             ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_9[P9, P10, P10, P11, A, B, C, D, E, F, a, b, c                                        ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_10[P10, P11, P11, P12, A, B, C, D, E, F, a, b, c, d                                    ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, F, a, b, c, d, e                                 ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, a, b, c, d, e, f                              ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, a, b, c, d, e, f, g                           ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, a, b, c, d, e, f, g, h                        ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j, k               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j, k, l            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j, k, l, m         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j, k, l, m, n      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ] (txMetaData: NS16[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p                  ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p] = ???

    }
  }

  trait Tx07[A, B, C, D, E, F, G] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_7[P7, P8, P8, P9, A, B, C, D, E, F, G                                                 ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_8[P8, P9, P9, P10, A, B, C, D, E, F, G, a                                             ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_9[P9, P10, P10, P11, A, B, C, D, E, F, G, a, b                                        ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_10[P10, P11, P11, P12, A, B, C, D, E, F, G, a, b, c                                    ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, F, G, a, b, c, d                                 ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, G, a, b, c, d, e                              ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, a, b, c, d, e, f                           ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, a, b, c, d, e, f, g                        ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h                     ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i, j               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i, j, k            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i, j, k, l         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i, j, k, l, m      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i, j, k, l, m, n   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ] (txMetaData: NS15[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o                     ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o] = ???
    }
  }

  trait Tx08[A, B, C, D, E, F, G, H] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_8[P8, P9, P9, P10, A, B, C, D, E, F, G, H                                             ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_9[P9, P10, P10, P11, A, B, C, D, E, F, G, H, a                                        ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_10[P10, P11, P11, P12, A, B, C, D, E, F, G, H, a, b                                    ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, F, G, H, a, b, c                                 ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, G, H, a, b, c, d                              ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, H, a, b, c, d, e                           ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, a, b, c, d, e, f                        ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g                     ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h                  ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h, i               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h, i, j            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h, i, j, k         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h, i, j, k, l      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h, i, j, k, l, m   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ] (txMetaData: NS14[a, b, c, d, e, f, g, h, i, j, k, l, m, n                        ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, a, b, c, d, e, f, g, h, i, j, k, l, m, n] = ???
    }
  }

  trait Tx09[A, B, C, D, E, F, G, H, I] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_9[P9, P10, P10, P11, A, B, C, D, E, F, G, H, I                                        ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_10[P10, P11, P11, P12, A, B, C, D, E, F, G, H, I, a                                    ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, F, G, H, I, a, b                                 ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, G, H, I, a, b, c                              ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, H, I, a, b, c, d                           ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, I, a, b, c, d, e                        ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f                     ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g                  ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g, h               ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g, h, i            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g, h, i, j         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g, h, i, j, k      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g, h, i, j, k, l   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l, m                           ] (txMetaData: NS13[a, b, c, d, e, f, g, h, i, j, k, l, m                           ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, a, b, c, d, e, f, g, h, i, j, k, l, m] = ???
    }
  }

  trait Tx10[A, B, C, D, E, F, G, H, I, J] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_10[P10, P11, P11, P12, A, B, C, D, E, F, G, H, I, J                                    ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, F, G, H, I, J, a                                 ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, G, H, I, J, a, b                              ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, H, I, J, a, b, c                           ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, I, J, a, b, c, d                        ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e                     ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f                  ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f, g               ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f, g, h            ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f, g, h, i         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f, g, h, i, j      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f, g, h, i, j, k   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k, l                              ] (txMetaData: NS12[a, b, c, d, e, f, g, h, i, j, k, l                              ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, a, b, c, d, e, f, g, h, i, j, k, l] = ???
    }
  }

  trait Tx11[A, B, C, D, E, F, G, H, I, J, K] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_11[P11, P12, P12, P13, A, B, C, D, E, F, G, H, I, J, K                                 ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, G, H, I, J, K, a                              ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, H, I, J, K, a, b                           ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, I, J, K, a, b, c                        ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d                     ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e                  ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e, f               ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e, f, g            ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e, f, g, h         ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e, f, g, h, i      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e, f, g, h, i, j   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j, k                                 ] (txMetaData: NS11[a, b, c, d, e, f, g, h, i, j, k                                 ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, a, b, c, d, e, f, g, h, i, j, k] = ???
    }
  }

  trait Tx12[A, B, C, D, E, F, G, H, I, J, K, L] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_12[P12, P13, P13, P14, A, B, C, D, E, F, G, H, I, J, K, L                              ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, H, I, J, K, L, a                           ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, I, J, K, L, a, b                        ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c                     ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d                  ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d, e               ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d, e, f            ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d, e, f, g         ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d, e, f, g, h      ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d, e, f, g, h, i   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i, j                                    ] (txMetaData: NS10[a, b, c, d, e, f, g, h, i, j                                    ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, a, b, c, d, e, f, g, h, i, j] = ???
    }
  }

  trait Tx13[A, B, C, D, E, F, G, H, I, J, K, L, M] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_13[P13, P14, P14, P15, A, B, C, D, E, F, G, H, I, J, K, L, M                           ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, I, J, K, L, M, a                        ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b                     ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c                  ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c, d               ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c, d, e            ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c, d, e, f         ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c, d, e, f, g      ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c, d, e, f, g, h   ] = ???
      final def apply[a, b, c, d, e, f, g, h, i                                       ] (txMetaData: NS09[a, b, c, d, e, f, g, h, i                                       ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, a, b, c, d, e, f, g, h, i] = ???
    }
  }

  trait Tx14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_14[P14, P15, P15, P16, A, B, C, D, E, F, G, H, I, J, K, L, M, N                        ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a                     ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b                  ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b, c               ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b, c, d            ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b, c, d, e         ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b, c, d, e, f      ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b, c, d, e, f, g   ] = ???
      final def apply[a, b, c, d, e, f, g, h                                          ] (txMetaData: NS08[a, b, c, d, e, f, g, h                                          ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, a, b, c, d, e, f, g, h] = ???
    }
  }

  trait Tx15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_15[P15, P16, P16, P17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O                     ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a                  ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a, b               ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a, b, c            ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a, b, c, d         ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a, b, c, d, e      ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a, b, c, d, e, f   ] = ???
      final def apply[a, b, c, d, e, f, g                                             ] (txMetaData: NS07[a, b, c, d, e, f, g                                             ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, a, b, c, d, e, f, g] = ???
    }
  }

  trait Tx16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_16[P16, P17, P17, P18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P                  ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, a               ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, a, b            ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, a, b, c         ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, a, b, c, d      ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, a, b, c, d, e   ] = ???
      final def apply[a, b, c, d, e, f                                                ] (txMetaData: NS06[a, b, c, d, e, f                                                ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, a, b, c, d, e, f] = ???
    }
  }

  trait Tx17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_17[P17, P18, P18, P19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q               ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, a            ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, a, b         ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, a, b, c      ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, a, b, c, d   ] = ???
      final def apply[a, b, c, d, e                                                   ] (txMetaData: NS05[a, b, c, d, e                                                   ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, a, b, c, d, e] = ???
    }
  }

  trait Tx18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_18[P18, P19, P19, P20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R            ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, a         ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, a, b      ] = ???
      final def apply[a, b, c                                                         ] (txMetaData: NS03[a, b, c                                                         ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, a, b, c   ] = ???
      final def apply[a, b, c, d                                                      ] (txMetaData: NS04[a, b, c, d                                                      ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, a, b, c, d] = ???
    }
  }

  trait Tx19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends Tx {
    object Tx {
      final def apply[dummy                                                           ](txMetaData: NS00[dummy                                                             ]): Out_19[P19, P20, P20, P21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S         ] = ???
      final def apply[a                                                               ](txMetaData: NS01[a                                                                 ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, a      ] = ???
      final def apply[a, b                                                            ](txMetaData: NS02[a, b                                                              ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, a, b   ] = ???
      final def apply[a, b, c                                                         ](txMetaData: NS03[a, b, c                                                           ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, a, b, c] = ???
    }
  }

  trait Tx20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_20[P20, P21, P21, P22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T      ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, a   ] = ???
      final def apply[a, b                                                            ] (txMetaData: NS02[a, b                                                            ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, a, b] = ???
    }
  }

  trait Tx21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_21[P21, P22, P22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U   ] = ???
      final def apply[a                                                               ] (txMetaData: NS01[a                                                               ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, a] = ???
    }
  }

  trait Tx22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends Tx {
    object Tx {
      final def apply[dummy                                                           ] (txMetaData: NS00[dummy                                                           ]): Out_22[P22, P23, P23, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    }
  }
}