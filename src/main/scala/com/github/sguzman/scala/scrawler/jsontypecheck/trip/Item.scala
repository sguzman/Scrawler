package com.github.sguzman.scala.scrawler.jsontypecheck.trip

sealed case class Item(
                      description: String,
                      icon: String,
                      amount: Double,
                      itemType: String,
                      shouldShowPlusSign: Boolean,
                      disclaimer: String,
                      recognizedAt: String
                      )