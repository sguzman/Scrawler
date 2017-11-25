package com.github.sguzman.scala.scrawler

import com.github.sguzman.scala.scrawler.jcommander.Args
import org.feijoas.mango.common.base.Preconditions

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)

    val url = "https://partners.uber.com/p3/platform_chrome_nav_data"
    val request = Http(url).header("Cookie", argv.cookie)
    val response = request.asString
    val success = response.code
    Preconditions.checkArgument(success == 200, "Cookie was not valid")

    val url2 = "https://partners.uber.com/p3/money/statements/all_data/"
    val request2 = Http(url2).header("Cookie", argv.cookie)
    val response2 = request2.asString
    println(response2.code)
    println(response2.body)
  }
}
