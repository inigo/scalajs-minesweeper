enablePlugins(ScalaJSPlugin, WorkbenchPlugin)

name := "scalajs-minesweeper"

organization := "net.surguy"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  , "com.lihaoyi" %%% "utest" % "0.4.4" % "test"
)

testFrameworks += new TestFramework("utest.runner.Framework")
