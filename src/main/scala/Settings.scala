package com.github.windymelt.obsidianatompub

import scala.scalajs.js

@js.native
trait AtomPubPluginSettingsJS extends js.Object {
  val xWsseHeader: String = js.native
  val apiUrl: String = js.native
}
object AtomPubPluginSettingsJS {
  // companion
  def apply(xWsseHeader: String, apiUrl: String) = js.Dynamic
    .literal(xWsseHeader = xWsseHeader, apiUrl = apiUrl)
    .asInstanceOf[AtomPubPluginSettingsJS]
}
case class AtomPubPluginSettings(val xWsseHeader: String, val apiUrl: String) {
  def toJS: AtomPubPluginSettingsJS =
    AtomPubPluginSettingsJS(xWsseHeader = xWsseHeader, apiUrl = apiUrl)
}
object AtomPubPluginSettings {
  def fromJS(j: AtomPubPluginSettingsJS): AtomPubPluginSettings =
    AtomPubPluginSettings(xWsseHeader = j.xWsseHeader, apiUrl = j.apiUrl)
}
