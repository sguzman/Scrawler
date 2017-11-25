package com.github.sguzman.scala.scrawler.jsontypecheck.statement

import java.util

sealed case class Statement(
                           statusCode: Int,
                           body: Body,
                           headers: util.HashMap[String,String],
                           request: UberRequest
                           )
