package com.github.sguzman.scala.scrawler.jsontypecheck.statement

import java.net.URL

sealed case class City(
                      locale: String,
                      timezone: String,
                      web_url: URL,
                      country_id: Int
                      )