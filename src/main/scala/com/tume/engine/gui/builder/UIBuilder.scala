package com.tume.engine.gui.builder

import com.tume.engine.gui._
import com.tume.engine.util.DisplayUtils

/**
  * Created by tume on 5/12/16.
  */
class UIBuilder(val uiComponent: UIComponent) {

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

  def above(another: UIBuilder, float: Float = 0): this.type = {
    uiComponent.y = another.uiComponent.y - uiComponent.height - toPixels(float)
    this
  }

  def below(another: UIBuilder, float: Float = 0): this.type = {
    uiComponent.y = another.uiComponent.y + another.uiComponent.height + toPixels(float)
    this
  }

  def leftOf(another: UIBuilder, float: Float = 0): this.type = {
    uiComponent.x = another.uiComponent.x - uiComponent.width - toPixels(float)
    this
  }

  def rightOf(another: UIBuilder, float: Float = 0): this.type = {
    uiComponent.x = another.uiComponent.x + another.uiComponent.width + toPixels(float)
    this
  }

  def xBetween(first: UIBuilder, second: UIBuilder) : this.type = {
    val c1 = first.uiComponent
    val c2 = second.uiComponent
    uiComponent.x = c1.x + c1.width + (c2.x - c1.x - c1.width) / 2 - uiComponent.width / 2
    this
  }

  def yBetween(first: UIBuilder, second: UIBuilder) : this.type = {
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

  def alignTop(uIBuilder: UIBuilder, f: Float = 0f) : this.type = {
    uiComponent.y = uIBuilder.uiComponent.y + toPixels(f)
    this
  }

  def alignLeft(uIBuilder: UIBuilder, f: Float = 0f) : this.type = {
    uiComponent.x = uIBuilder.uiComponent.x + toPixels(f)
    this
  }

  def alignRight(uIBuilder: UIBuilder, f: Float = 0f) : this.type = {
    uiComponent.x = uIBuilder.uiComponent.x + uIBuilder.uiComponent.width - uiComponent.width - toPixels(f)
    this
  }

  def alignBottom(uIBuilder: UIBuilder, f: Float = 0f) : this.type = {
    uiComponent.y = uIBuilder.uiComponent.y + uIBuilder.uiComponent.height - uiComponent.height - toPixels(f)
    this
  }

  def alignCenter(uIBuilder: UIBuilder) : this.type = {
    uiComponent.x = uIBuilder.uiComponent.x + uIBuilder.uiComponent.width / 2 - uiComponent.width / 2
    uiComponent.y = uIBuilder.uiComponent.y + uIBuilder.uiComponent.height / 2 - uiComponent.height / 2
    this
  }

  def resolve = uiComponent
}
object UIBuilder {
  def apply(uIComponent: UIComponent) : UIBuilder = new UIBuilder(uIComponent)
  def button = new UIButtonBuilder()
  def space = UIBuilder(new UISpace())
  def panel = UIBuilder(new UIPanel())
  def label = new UILabelBuilder()
  def instantButton = new UIButtonBuilder(new UIInstantButton())
  def panelButton = new UIButtonBuilder(new UIPanelToggleButton())
  def progressBar = new UIProgressBarBuilder()
}
object UIBuilderSettings {
  var padding = Math.round(DisplayUtils.scale)
}

