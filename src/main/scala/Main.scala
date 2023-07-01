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
      println("creating ribbonIcon")
      val ribbonIconEl = this.addRibbonIcon(
        icon = "dice",
        title = "Sample Plugin",
        callback = _ => Notice("This is a notice!")
      )
      println("adding ribbon class")
      ribbonIconEl.addClass("my-plugin-ribbon-class")
      println("ribbon cladd added.")

      println("setting statusbarItem")
      val statusBarItemEl = this.addStatusBarItem()
      statusBarItemEl.setText("Status Bar Text")
      println("statusbarItem done")

      println("adding command 1")
      this.addCommand {
        val c = Command(
          "open-sample-modal-simple",
          "Open sample modal (simple)"
        )
        c.callback = () => SampleModal(this.app).`open`()
        c
      }
      println("added command 1")

      println("adding command 2")
      this.addCommand {
        val c = Command("sample-editor-command", "Sample editor command")
        c.editorCallback = (e, v) => {
          println(e.getSelection())
          e.replaceSelection("Sample Editor Command")
        }
        c
      }
      println("added command 2")

      println("setting tab")
      this.addSettingTab(SampleSettingTab(this.app, this))

      println("plugin loaded!")
    }
  }
  override def onunload(): Unit = {}
  def loadSettings: Future[Unit] = for {
    _ <- Future { println("loading settings!") }
    setting <- this.loadData()
    // TODO: merge setting and assign
  } yield {
    if (setting != null) {
      // TODO: なにやら変形して設定を読み出して設定する
      // settingはどうやらObjectで返却される。このフィールドをうまく取り出してAtomPubPluginSettingsの形にする必要がある。
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

  println("constructing samplesettingtab")

  override def display(): Any = {
    println("displaying")
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
            println(s"secret: $v")
            val newSettings = AtomPubPluginSettings
              .fromJS(this.plugin.settings)
              .copy(mySetting = v)
              .toJS
            plugin.settings = newSettings
            println(s"new setting: $newSettings")
            this.plugin.saveSettings
          })
      )
  }
}
