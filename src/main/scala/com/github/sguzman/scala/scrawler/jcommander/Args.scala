package com.github.sguzman.scala.scrawler.jcommander

import com.beust.jcommander.{JCommander, Parameter}

class Args {
  @Parameter(
    names = Array("-c", "--cookie"),
    description = "Uber cookie",
    arity = 1,
    help = false,
    echoInput = false,
    password = true,
    hidden = false,
    order = 1,
    required = true,
  )
  var cookie: String = ""

  @Parameter(
    names = Array("-h", "--help"),
    description = "Help menu",
    arity = 0,
    help = true,
    echoInput = true,
    password = false,
    hidden = false,
    order = 5,
    required = false,
  )
  var help: Boolean = false
}

object Args {
  def apply(args: Array[String]): Args = {
    val argv = new Args
    val j = JCommander.newBuilder.addObject(argv).build
    j.parse(args: _*)

    if (argv.help) {
      j.usage()
      System.exit(0)
    }

    argv
  }
}
