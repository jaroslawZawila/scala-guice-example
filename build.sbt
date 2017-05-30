lazy val commonSettings = Seq(
  organization := "org.zawila",
  version := "0.1.0",
  // set the Scala version used for the project
  scalaVersion := "2.12.1"
)

libraryDependencies ++= Seq(
  "com.google.inject"   % "guice"           % "4.1.0",
  "org.scalatest"       % "scalatest_2.12"  % "3.0.3"
)