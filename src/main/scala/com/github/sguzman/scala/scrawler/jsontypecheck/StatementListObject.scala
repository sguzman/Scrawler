package com.github.sguzman.scala.scrawler.jsontypecheck

import java.util.UUID

sealed case class StatementListObject(
                   partner_uuid: UUID,
                   status: String,
                   city_id: Int,
                   total: String,
                   id: Option[String],
                   payout_type: Option[String],
                   payee_type: String,
                   uuid: UUID,
                   currency_code: String,
                   ending_at: Long,
                   num_txns: Int
)