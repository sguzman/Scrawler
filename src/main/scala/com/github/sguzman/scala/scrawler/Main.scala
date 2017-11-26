package com.github.sguzman.scala.scrawler

import java.util.UUID

import com.github.sguzman.scala.scrawler.jsontypecheck.StatementListObject
import com.github.sguzman.scala.scrawler.jsontypecheck.`return`.Trips
import com.github.sguzman.scala.scrawler.jsontypecheck.statement.Statement
import com.github.sguzman.scala.scrawler.jsontypecheck.trip.Trip
import com.google.gson.GsonBuilder
import lol.http._
import scala.concurrent.ExecutionContext.Implicits.global
import org.feijoas.mango.common.base.Preconditions

import scalaj.http.{Http, HttpResponse}

object Main {
  var json = "{}"
  def main(args: Array[String]): Unit = {
    val cookie = System.getenv("COOKIE")
    val port = System.getenv("PORT")

    Server.listen(port.toInt) {
      case GET at url"/" =>
        Ok(json).addHeaders(
          (HttpString("Access-Control-Allow-Origin"), HttpString("*")),
          (HttpString("Access-Control-Allow-Headers"), HttpString("Origin, X-Requested-With, Content-Type, Accept"))

      case _ =>
        NotFound
    }

    val response = getStatus(cookie)
    val success = response.code
    Preconditions.checkArgument(success == 200, "Cookie was not valid")

    val jsonTypes = getStatementList(cookie)

    val stmts = getStatement(cookie, _: StatementListObject)
    val statements = jsonTypes.par.map(stmts).filter(_.isDefined).map(_.get)
    val tripUuids = statements.map(_.body.driver.trip_earnings.trips.keySet).flatMap(_.toArray).map(_.asInstanceOf[UUID])

    val trp = getTrip(cookie, _: UUID)
    val trips = tripUuids.par.map(trp).filter(_.isDefined).map(_.get)

    val tripMe = Trips(trips.toArray)
    this.json = new GsonBuilder().create.toJson(tripMe)
    println("done")
  }

  def getTrip(cookie: String, uuid: UUID): Option[Trip] = {
    try {
      val uuidString = uuid.toString
      val url = s"https://partners.uber.com/p3/money/trips/trip_data/$uuidString"

      val request = Http(url).header("Cookie", cookie)
      val response = request.asString

      val gson = new GsonBuilder().create
      val jsonType = gson.fromJson(response.body, classOf[Trip])
      println(s"Trip uuid $uuid")
      Some(jsonType)
    } catch {
      case _: Throwable =>
        Console.err.println(s"Failed at uuid $uuid")
        None
    }
  }

  def getStatement(cookie: String, statement: StatementListObject): Option[Statement] = {
    try {
      val uuid = statement.uuid.toString
      val url = s"https://partners.uber.com/p3/money/statements/view/$uuid"
      val request = Http(url).header("Cookie", cookie)
      val response = request.asString
      val gson = new GsonBuilder().create

      val jsonType = gson.fromJson(response.body, classOf[Statement])

      println(s"Statement $uuid")
      Some(jsonType)
    } catch {
      case _: Throwable =>
        Console.err.println(s"Failed at uuid ${statement.uuid}")
        None
    }
  }

  def getStatus(cookie: String): HttpResponse[String] = {
    val url = "https://partners.uber.com/p3/platform_chrome_nav_data"
    val request = Http(url).header("Cookie", cookie)
    val response = request.asString

    response
  }

  def getStatementList(cookie: String): Array[StatementListObject] = {
    val url = "https://partners.uber.com/p3/money/statements/all_data/"
    val request = Http(url).header("Cookie", cookie)
    val response = request.asString
    val gson = new GsonBuilder().create()
    val jsonTypes = gson.fromJson(response.body, classOf[Array[StatementListObject]])

    jsonTypes
  }
}
