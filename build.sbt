lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.5.21"
lazy val elastic4sVersion = "6.5.1"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.7"
    )),
    name := "choucas",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,

      "org.scalaj" %% "scalaj-http" % "2.4.1",
      "io.spray" %%  "spray-json" % "1.3.5",
      "com.typesafe.play" %% "play-json" % "2.6.10",

      "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,

      // for the http client
      "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,

      // if you want to use reactive streams
      "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion,

      // testing
      "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test",
      "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion % "test"
    )
  )
