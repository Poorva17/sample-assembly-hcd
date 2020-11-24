import sbt._

object Libs {
  val ScalaVersion = "2.12.6"

  val `scalatest`       = "org.scalatest"          %% "scalatest"      % "3.0.5"  //Apache License 2.0
  val `scala-async`     = "org.scala-lang.modules" %% "scala-async"    % "0.9.7"  //BSD 3-clause "New" or "Revised" License
  val `junit`           = "junit"                  % "junit"           % "4.12"   //Eclipse Public License 1.0
  val `junit-interface` = "com.novocode"           % "junit-interface" % "0.11"   //BSD 2-clause "Simplified" License
  val `mockito-core`    = "org.mockito"            % "mockito-core"    % "2.16.0" //MIT License
  val `mqtt`            = "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.2.0"
}

object CSW {
  //for testing local changes use following
  //private val Org     = "org.tmt"
  //private val Version = "0.1-SNAPSHOT"
  private val Org     = "com.github.tmtsoftware.csw-prod"
  private val Version = "0581dc77e"

  val `csw-location`      = Org %% "csw-location"      % Version
  val `csw-config-client` = Org %% "csw-config-client" % Version
  val `csw-logging`       = Org %% "csw-logging"       % Version
  val `csw-framework`     = Org %% "csw-framework"     % Version
  val `csw-command`       = Org %% "csw-command"       % Version
  val `csw-messages`      = Org %% "csw-messages"      % Version
}
