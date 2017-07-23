
scalaVersion in ThisBuild := "2.12.2"

lazy val root = project.in(file(".")).
  aggregate(minesweeperJS, minesweeperJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val minesweeper = crossProject.in(file(".")).
  settings(
    name := "scalajs-minesweeper",
    organization := "net.surguy",
    version := "0.1-SNAPSHOT"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "3.9.4" % "test"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1"
      , "com.lihaoyi" %%% "scalatags" % "0.6.5"
      , "com.lihaoyi" %%% "utest" % "0.4.4" % "test"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    jsDependencies += RuntimeDOM
  )

lazy val minesweeperJVM = minesweeper.jvm
lazy val minesweeperJS = minesweeper.js
  .enablePlugins(WorkbenchPlugin)

testFrameworks += new TestFramework("utest.runner.Framework")

import S3._
s3Settings
mappings in upload := Seq((new java.io.File("js/target/scala-2.12/scalajs-minesweeper-opt.js"),"scalajs-minesweeper-opt.js"),
  (new java.io.File("js/target/scala-2.12/classes/index-opt.html"),"resources/index-opt.html"))
host in upload := "scalajs-minesweeper.s3.amazonaws.com"