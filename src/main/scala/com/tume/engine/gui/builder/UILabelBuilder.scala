package com.tume.engine.gui.builder

import com.tume.engine.gui.{UILabel, UIButton}

/**
  * Created by tume on 8/9/16.
  */
class UILabelBuilder(private val label: UILabel = new UILabel()) extends UIBuilder[UILabel](label) {

  def img(drawable: Int) : this.type = {
    uiComponent.iconResource = drawable
    this
  }

  def text(text: String) : this.type = {
    uiComponent.text = text
    this
  }

  def tSize(size: Int) : this.type = {
    uiComponent.textSize = size
    this
  }

  def centerText : this.type = {
    uiComponent.centerText = true
    this
  }
}
