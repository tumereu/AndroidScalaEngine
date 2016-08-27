package com.tume.engine.gui.builder

import com.tume.engine.gui._
import com.tume.engine.util.{Calc, DisplayUtils}
import com.tume.scalaengine.R

/**
  * Created by tume on 5/12/16.
  */
class UIBuilder[T <: UIComponent](val uiComponent: T) {

  private def toPixels(float: Float) = (float * DisplayUtils.screenSize).toInt

  def width(float: Float) : this.type = {
    uiComponent.width = toPixels(float)
    this
  }

  def height(float: Float) : this.type = {
    uiComponent.height = toPixels(float)
    this
  }

  def absHeight(float: Float) : this.type = {
    uiComponent.height = (float * DisplayUtils.screenHeight).toInt
    this
  }

  def absWidth(float: Float) : this.type = {
    uiComponent.width = (float * DisplayUtils.screenWidth).toInt
    this
  }

  def size(float: Float) : this.type = width(float).height(float)

  def x(float: Float) : this.type = {
    uiComponent.x = toPixels(float)
    this
  }

  def y(float: Float) : this.type = {
    uiComponent.y = toPixels(float)
    this
  }

  def ax(float: Float) : this.type = {
    uiComponent.x = (float * DisplayUtils.screenWidth).toInt
    this
  }

  def ay(float: Float) : this.type = {
    uiComponent.y = (float * DisplayUtils.screenHeight).toInt
    this
  }

  def top(float: Float = 0) : this.type = {
    uiComponent.y = toPixels(float)
    this
  }

  def bottom(float: Float = 0) : this.type = {
    uiComponent.y = DisplayUtils.screenHeight - uiComponent.height - toPixels(float)
    this
  }

  def left(float: Float = 0) : this.type = {
    uiComponent.x = toPixels(float)
    this
  }

  def right(float: Float = 0) : this.type = {
    uiComponent.x = DisplayUtils.screenWidth - uiComponent.width - toPixels(float)
    this
  }

  def above(another: UIBuilder[_ <: UIComponent], float: Float = 0): this.type = {
    uiComponent.y = another.uiComponent.y - uiComponent.height - toPixels(float)
    this
  }

  def below(another: UIBuilder[_ <: UIComponent], float: Float = 0): this.type = {
    uiComponent.y = another.uiComponent.y + another.uiComponent.height + toPixels(float)
    this
  }

  def leftOf(another: UIBuilder[_ <: UIComponent], float: Float = 0): this.type = {
    uiComponent.x = another.uiComponent.x - uiComponent.width - toPixels(float)
    this
  }

  def rightOf(another: UIBuilder[_ <: UIComponent], float: Float = 0): this.type = {
    uiComponent.x = another.uiComponent.x + another.uiComponent.width + toPixels(float)
    this
  }

  def xBetween(first: UIBuilder[_ <: UIComponent], second: UIBuilder[_ <: UIComponent]) : this.type = {
    val c1 = first.uiComponent
    val c2 = second.uiComponent
    uiComponent.x = c1.x + c1.width + (c2.x - c1.x - c1.width) / 2 - uiComponent.width / 2
    this
  }

  def yBetween(first: UIBuilder[_ <: UIComponent], second: UIBuilder[_ <: UIComponent]) : this.type = {
    val c1 = first.uiComponent
    val c2 = second.uiComponent
    uiComponent.y = c1.y + c1.height + (c2.y - c1.y - c1.height) / 2 - uiComponent.height / 2
    this
  }

  def id(string: String) : this.type = {
    uiComponent.id = Some(string)
    this
  }

  def pad(padding: Int = UIBuilderSettings.padding) : this.type = {
    uiComponent.x += padding
    uiComponent.y += padding
    uiComponent.width -= padding * 2
    uiComponent.height -= padding * 2
    this
  }

  def alignTop(uIBuilder: UIBuilder[_ <: UIComponent], f: Float = 0f) : this.type = {
    uiComponent.y = uIBuilder.uiComponent.y + toPixels(f)
    this
  }

  def alignLeft(uIBuilder: UIBuilder[_ <: UIComponent], f: Float = 0f) : this.type = {
    uiComponent.x = uIBuilder.uiComponent.x + toPixels(f)
    this
  }

  def alignRight(uIBuilder: UIBuilder[_ <: UIComponent], f: Float = 0f) : this.type = {
    uiComponent.x = uIBuilder.uiComponent.x + uIBuilder.uiComponent.width - uiComponent.width - toPixels(f)
    this
  }

  def alignBottom(uIBuilder: UIBuilder[_ <: UIComponent], f: Float = 0f) : this.type = {
    uiComponent.y = uIBuilder.uiComponent.y + uIBuilder.uiComponent.height - uiComponent.height - toPixels(f)
    this
  }

  def alignCenter(uIBuilder: UIBuilder[_ <: UIComponent]) : this.type = {
    uiComponent.x = uIBuilder.uiComponent.x + uIBuilder.uiComponent.width / 2 - uiComponent.width / 2
    uiComponent.y = uIBuilder.uiComponent.y + uIBuilder.uiComponent.height / 2 - uiComponent.height / 2
    this
  }

  def stretchX(uIBuilder1: UIBuilder[_ <: UIComponent], uIBuilder2: UIBuilder[_ <: UIComponent]) : this.type = {
    uiComponent.width = uIBuilder2.uiComponent.x - uIBuilder1.uiComponent.x - uIBuilder1.uiComponent.width
    this.rightOf(uIBuilder1)
  }

  def stretchY(uIBuilder1: UIBuilder[_ <: UIComponent], uIBuilder2: UIBuilder[_ <: UIComponent]) : this.type = {
    uiComponent.height = uIBuilder2.uiComponent.y - uIBuilder1.uiComponent.y - uIBuilder1.uiComponent.height
    this.below(uIBuilder1)
  }

  def tooltip(string: String) : this.type = {
    uiComponent.tooltip = Option(string)
    this
  }

  def resolve = uiComponent
}
object UIBuilder {
  def apply[T <: UIComponent](uIComponent: T) : UIBuilder[T] = new UIBuilder[T](uIComponent)
  def button = new UIButtonBuilder()
  def space = UIBuilder(new UISpace())
  def panel = UIBuilder(new UIPanel())
  def label = new UILabelBuilder()
  def instantButton = new UIButtonBuilder(new UIInstantButton())
  def radioButton = new UIRadioButtonBuilder()
  def panelButton = new UIButtonBuilder(new UIPanelToggleButton())
  def progressBar = new UIProgressBarBuilder()
  def popupPanel = new UIPopupPanelBuilder()

  def selectionDialog(id: String, cols: Int, rows: Int) : UIPopupPanelBuilder = {
    val panel = new UIPopupPanelBuilder(new UISelectionPopupPanel).id(id)
    val headerHeight = 0.125f
    val bt1 = button
    val bt2 = button
    val lab = label
    panel += bt1.size(headerHeight).top(0.3f).left(0.05f).img(R.mipmap.ic_arrow_left).id(id + "_page_left")
    panel += bt2.size(headerHeight).alignTop(bt1).rightOf(bt1, 0.9f - headerHeight * 2).img(R.mipmap.ic_arrow_right).id(id + "_page_right")
    panel += lab.alignTop(bt1).height(headerHeight).stretchX(bt1, bt2).text("Page 1/1").centerText.id(id + "_header")
    var builders = Vector(bt1, bt2, lab)
    val btnSize = 0.9f / cols
    for (y <- 0 until rows; x <- 0 until cols) {
      val b = button.size(btnSize).alignLeft(bt1, btnSize * x).below(bt1, y * btnSize).id(id + "_button" + (y * cols + x))
      panel += b
      builders = builders :+ b
    }
    builders.foreach(_.pad())
    panel
  }
}
object UIBuilderSettings {
  var padding = Calc.round(DisplayUtils.scale)
}

