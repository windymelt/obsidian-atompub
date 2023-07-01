package com.github.windymelt.obsidianatompub

@main def hello: Unit =
  println("Hello world!")
  println(msg)

def msg = "I was compiled by Scala 3. :)"

import typings.obsidian.mod.{
  App,
  Editor,
  MarkdownView,
  Modal,
  Notice,
  Plugin,
  PluginSettingTab,
  Setting
}
import scala.concurrent.Future
// Future <==> Promise of ScalaJS
import scala.scalajs.js.Thenable.Implicits._
import typings.obsidian.mod.SettingTab
import typings.estree.estreeStrings.tag
import typings.obsidian.obsidianStrings.h2
import typings.obsidian.obsidianStrings
import scala.scalajs.js.annotation.JSExportTopLevel
import typings.obsidian.mod.Command
import typings.std.stdStrings.document
import typings.codemirror.codemirrorStrings.click
import typings.obsidian.mod.PluginManifest
import scala.scalajs.js.Promise
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
//import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js

implicit val ec: scala.concurrent.ExecutionContext =
  scala.concurrent.ExecutionContext.global

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

val DEFAULT_SETTINGS: AtomPubPluginSettingsJS =
  AtomPubPluginSettingsJS(mySetting = "DEFAULT_SETTING_VAR")

@JSExportTopLevel("default")
class AtomPubPlugin(app: App, manifest: PluginManifest)
    extends Plugin(app, manifest) {
  var settings: AtomPubPluginSettingsJS = DEFAULT_SETTINGS

  override def onload(): Unit = {
    for {
      _ <- this.loadSettings
    } yield {
      val ribbonIconEl = this.addRibbonIcon(
        icon = "dice",
        title = "Sample Plugin",
        callback = _ => Notice("This is a notice!")
      )
      ribbonIconEl.addClass("my-plugin-ribbon-class")

      val statusBarItemEl = this.addStatusBarItem()
      statusBarItemEl.setText("Status Bar Text")

      this.addCommand {
        val c = Command(
          "open-sample-modal-simple",
          "Open sample modal (simple)"
        )
        c.callback = () => SampleModal(this.app).`open`()
        c
      }
      this.addCommand {
        val c = Command("sample-editor-command", "Sample editor command")
        c.editorCallback = (e, v) => {
          println(e.getSelection())
          e.replaceSelection("Sample Editor Command")
        }
        c
      }
      this.addSettingTab(SampleSettingTab(this.app, this))

      println("AtomPub plugin loaded!")
    }
  }
  override def onunload(): Unit = {}
  def loadSettings: Future[Unit] = for {
    setting <- this.loadData()
  } yield {
    if (setting != null) {
      val settings = setting.asInstanceOf[AtomPubPluginSettingsJS]
      this.settings = settings
    }
  }
  def saveSettings: Future[Unit] = this.saveData(this.settings)
}

class SampleModal(app: App) extends Modal(app) {
  override def onOpen(): Unit = {
    this.contentEl.setText("Wooah!!")
  }
  override def onClose(): Unit = {
    this.contentEl.empty()
  }
}

class SampleSettingTab(app: App, val plugin: AtomPubPlugin)
    extends PluginSettingTab(app, plugin) {

  override def display(): Any = {
    this.containerEl.empty()
    val inner =
      new typings.obsidian.publishMod.global.DomElementInfo {
        text = "Settings for my awesome plugin."
      }
    this.containerEl.createEl_h2(obsidianStrings.h2, inner)

    new Setting(this.containerEl)
      .setName("Setting #1")
      .setDesc("It's a secret")
      .addText(t =>
        t.setPlaceholder("Enter your secret")
          .setValue(this.plugin.settings.mySetting)
          .onChange(v => {
            val newSettings = AtomPubPluginSettings
              .fromJS(this.plugin.settings)
              .copy(mySetting = v)
              .toJS
            plugin.settings = newSettings
            this.plugin.saveSettings
          })
      )
  }
}
