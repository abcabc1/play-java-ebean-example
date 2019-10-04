name := "play-java-ebean-example"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test

libraryDependencies ++= Seq(
  javaWs
  //,caffeine
  ,play.sbt.PlayImport.cacheApi
  ,"mysql" % "mysql-connector-java" % "8.0.17"
  ,"org.beanshell" % "bsh" % "2.0b5"
  ,"com.github.stuxuhai" % "jpinyin" % "1.1.8"
  ,"net.coobird" % "thumbnailator" % "0.4.8"
  ,"com.github.karelcemus" %% "play-redis" % "2.5.0"
  ,"com.graphql-java" % "graphql-java" % "2019-09-22T22-20-34-9e83320"
  ,"com.graphql-java" % "graphql-java-tools" % "5.2.4"
  , "org.jsoup" % "jsoup" % "1.12.1"


)
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")
