/** Name of project */
name := "Scrawler"

/** Organization */
organization := "com.github.sguzman"

/** Project Version */
version := "1.0"

/** Scala version */
scalaVersion := "2.12.4"

/** Scalac parameters */
scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

/** Javac parameters */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

/** Resolver */
resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.sonatypeRepo("public"),
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.jcenterRepo,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
)

/** Source Dependencies */
libraryDependencies ++= Seq(
  "org.scalaj" % "scalaj-http_2.12" % "2.3.0",
  "com.google.code.gson" % "gson" % "2.8.2",
  "org.feijoas" % "mango_2.12" % "0.14",
  "com.criteo.lolhttp" % "lolhttp_2.12" % "0.8.1",
)

/** Make sure to fork on run */
fork in run := true


herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value)