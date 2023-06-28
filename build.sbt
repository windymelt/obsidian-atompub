val scala3Version = "3.3.0"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "obsidian-atompub",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    npmDependencies in Compile += "obsidian" -> "1.2.8"
  )
