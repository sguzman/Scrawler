package com.github.sguzman.scala.scrawler

import com.github.sguzman.scala.scrawler.jcommander.Args
import com.github.sguzman.scala.scrawler.jsontypecheck.StatementListObject
import com.github.sguzman.scala.scrawler.jsontypecheck.statement.Statement
import com.google.gson.GsonBuilder
import org.feijoas.mango.common.base.Preconditions

import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)

    val response = getStatus(argv)
    val success = response.code
    Preconditions.checkArgument(success == 200, "Cookie was not valid")

    val jsonTypes = getStatementList(argv)

    val stmts = getStatement(argv, _: StatementListObject)
    val statements = jsonTypes.par.map(stmts).filter(_.isDefined).map(_.get)
    val tripUuids = statements.map(_.body.driver.trip_earnings.trips.keySet).flatMap(_.toArray)

    println(tripUuids)
  }

  def getStatement(argv: Args, statement: StatementListObject) = {
    try {
      val uuid = statement.uuid.toString
      println(uuid)
      val url = s"https://partners.uber.com/p3/money/statements/view/$uuid"
      val request = Http(url).header("Cookie", argv.cookie)
      val response = request.asString
      val gson = new GsonBuilder().create

      val jsonType = gson.fromJson(response.body, classOf[Statement])

      Some(jsonType)
    } catch {
      case _: Throwable => None
    }
  }

  def getStatus(argv: Args): HttpResponse[String] = {
    val url = "https://partners.uber.com/p3/platform_chrome_nav_data"
    val request = Http(url).header("Cookie", argv.cookie)
    val response = request.asString

    response
  }

  def getStatementList(argv: Args): Array[StatementListObject] = {
    val url = "https://partners.uber.com/p3/money/statements/all_data/"
    val request = Http(url).header("Cookie", argv.cookie)
    val response = request.asString
    val gson = new GsonBuilder().create()
    val jsonTypes = gson.fromJson(response.body, classOf[Array[StatementListObject]])

    jsonTypes
  }
}
