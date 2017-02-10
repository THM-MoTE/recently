
lazy val root = Project(id = "recently", base = file(".")).
  settings(
    organization := "de.thm.mote",
    name := "recently",
    version := "0.1",
    scalaVersion := "2.11.8",
    javacOptions ++= Seq("-source", "1.8")
  )

libraryDependencies ++=
  Seq("io.spray" %%  "spray-json" % "1.3.3",
      "org.scalatest" %% "scalatest" % "2.2.6" % Test)
