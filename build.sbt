enablePlugins(ScalaJSPlugin, WorkbenchPlugin)

name := "scalajs-minesweeper"

organization := "net.surguy"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  , "com.lihaoyi" %%% "scalatags" % "0.6.5"
  , "com.lihaoyi" %%% "utest" % "0.4.4" % "test"
)

testFrameworks += new TestFramework("utest.runner.Framework")

import S3._
s3Settings
mappings in upload := Seq((new java.io.File("target/scala-2.12/scalajs-minesweeper-opt.js"),"scalajs-minesweeper-opt.js"),
  (new java.io.File("target/scala-2.12/classes/index-opt.html"),"resources/index-opt.html"))
host in upload := "scalajs-minesweeper.s3.amazonaws.com"