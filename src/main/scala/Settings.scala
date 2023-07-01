package com.github.windymelt.obsidianatompub

import scala.scalajs.js

@js.native
trait AtomPubPluginSettingsJS extends js.Object {
  val mySetting: String = js.native
}
object AtomPubPluginSettingsJS {
  // companion
  def apply(mySetting: String) = js.Dynamic
    .literal(mySetting = mySetting)
    .asInstanceOf[AtomPubPluginSettingsJS]
}
case class AtomPubPluginSettings(val mySetting: String) {
  def toJS: AtomPubPluginSettingsJS =
    AtomPubPluginSettingsJS(mySetting = mySetting)
}
object AtomPubPluginSettings {
  def fromJS(j: AtomPubPluginSettingsJS): AtomPubPluginSettings =
    AtomPubPluginSettings(mySetting = j.mySetting)
}
