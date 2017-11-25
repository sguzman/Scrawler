package com.github.sguzman.scala.scrawler.jsontypecheck.statement

import java.util.UUID

sealed case class TripChaining(
                              chain_index: String,
                              normalize_distance: Double,
                              chain_uuid: UUID,
                              normalized_duration: Int
                              )
