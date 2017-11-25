package com.github.sguzman.scala.scrawler.jsontypecheck.statement

sealed case class MiscNonFeeBreakdown(
                                     total: Double,
                                     items: Array[Item]
                                     )
