package com.github.sguzman.scala.scrawler.jsontypecheck.statement

sealed case class Totals(
                        fare: Double,
                        uber_fee: Double,
                        surge: Double,
                        cancellation: Double
                        )