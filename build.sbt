val scala3Version = "3.3.0"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .enablePlugins(ScalablyTypedConverterPlugin)
  .settings(
    name := "obsidian-atompub",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    Compile / npmDependencies += "obsidian" -> "1.2.8"
  )
