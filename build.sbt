
scalaVersion in ThisBuild := "2.13.6"

Global / onChangedBuildSource := ReloadOnSourceChanges

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
    version := "0.2"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "4.13.0" % "test"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.2.0"
      , "com.lihaoyi" %%% "scalatags" % "0.9.4"
      , "com.lihaoyi" %%% "utest" % "0.7.10" % "test"
    )
  )

lazy val minesweeperJVM = minesweeper.jvm
lazy val minesweeperJS = minesweeper.js

testFrameworks += new TestFramework("utest.runner.Framework")

enablePlugins(S3Plugin)

s3Upload / mappings := Seq((new java.io.File("js/target/scala-2.13/scalajs-minesweeper-opt.js"),"scalajs-minesweeper-opt.js"),
  (new java.io.File("js/target/scala-2.13/classes/index-opt.html"),"resources/index-opt.html"))
s3Upload / s3Host  := "scalajs-minesweeper.s3-eu-west-2.amazonaws.com"

