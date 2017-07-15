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

jsDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.3" / "2.1.3/jquery.js"
  , RuntimeDOM
)

testFrameworks += new TestFramework("utest.runner.Framework")
