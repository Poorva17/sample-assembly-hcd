import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {

  val Sample1Assembly = Seq(
    CSW.`csw-framework`,
    CSW.`csw-command`,
    CSW.`csw-location`,
    CSW.`csw-messages`,
    CSW.`csw-logging`,
    Libs.`mqtt`,
    Libs.`scalatest` % Test,
    Libs.`junit` % Test,
    Libs.`junit-interface` % Test
  )

  val Sample1Hcd = Seq(
    CSW.`csw-framework`,
    CSW.`csw-command`,
    CSW.`csw-location`,
    CSW.`csw-messages`,
    CSW.`csw-logging`,
    Libs.`mqtt`,
    Libs.`scalatest` % Test,
    Libs.`junit` % Test,
    Libs.`junit-interface` % Test
  )

  val Sample1Deploy = Seq(
    CSW.`csw-framework`
  )
}
