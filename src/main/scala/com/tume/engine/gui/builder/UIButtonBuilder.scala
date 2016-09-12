package com.tume.engine.gui.builder

import com.tume.engine.gui.UIButton

/**
  * Created by tume on 8/8/16.
  */
class UIButtonBuilder[T <: UIButton](private val button: T = new UIButton()) extends UIBuilder[T](button) {

  def img(drawable: Int) : this.type = {
    uiComponent.imageResource = drawable
    this
  }

  def instant : this.type = {
    uiComponent.instant = true
    this
  }
}
