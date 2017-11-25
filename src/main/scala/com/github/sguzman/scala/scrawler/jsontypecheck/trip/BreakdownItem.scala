package com.github.sguzman.scala.scrawler.jsontypecheck.trip

sealed case class BreakdownItem(
                               category: String,
                               items: Array[Item],
                               total: Double,
                               description: String
                               )