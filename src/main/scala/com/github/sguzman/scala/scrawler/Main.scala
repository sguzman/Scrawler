package com.github.sguzman.scala.scrawler

import java.util.UUID

import com.github.sguzman.scala.scrawler.jcommander.Args
import com.github.sguzman.scala.scrawler.jsontypecheck.StatementListObject
import com.github.sguzman.scala.scrawler.jsontypecheck.statement.Statement
import com.github.sguzman.scala.scrawler.jsontypecheck.trip.Trip
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
    val tripUuids = statements.map(_.body.driver.trip_earnings.trips.keySet).flatMap(_.toArray).map(_.asInstanceOf[UUID])

    val trp = getTrip(argv, _: UUID)
    val trips = tripUuids.par.map(trp).filter(_.isDefined).map(_.get)

    println(trips)
  }

  def getTrip(argv: Args, uuid: UUID): Option[Trip] = {
    try {
      val uuidString = uuid.toString
      println(s"Trip uuid $uuid")
      val url = s"https://partners.uber.com/p3/money/trips/trip_data/$uuidString"

      val request = Http(url).header("Cookie", argv.cookie)
      val response = request.asString

      val gson = new GsonBuilder().create
      val jsonType = gson.fromJson(response.body, classOf[Trip])
      Some(jsonType)
    } catch {
      case _: Throwable => None
    }
  }

  def getStatement(argv: Args, statement: StatementListObject): Option[Statement] = {
    try {
      val uuid = statement.uuid.toString
      println(s"Statement $uuid")
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
