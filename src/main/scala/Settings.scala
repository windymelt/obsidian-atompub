package com.github.windymelt.obsidianatompub

import scala.scalajs.js

@js.native
trait AtomPubPluginSettingsJS extends js.Object {
  val xWsseHeader: String = js.native
}
object AtomPubPluginSettingsJS {
  // companion
  def apply(xWsseHeader: String) = js.Dynamic
    .literal(xWsseHeader = xWsseHeader)
    .asInstanceOf[AtomPubPluginSettingsJS]
}
case class AtomPubPluginSettings(val xWsseHeader: String) {
  def toJS: AtomPubPluginSettingsJS =
    AtomPubPluginSettingsJS(xWsseHeader = xWsseHeader)
}
object AtomPubPluginSettings {
  def fromJS(j: AtomPubPluginSettingsJS): AtomPubPluginSettings =
    AtomPubPluginSettings(xWsseHeader = j.xWsseHeader)
}
