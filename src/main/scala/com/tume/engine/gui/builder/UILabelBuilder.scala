package com.tume.engine.gui.builder

import com.tume.engine.gui.{UILabel, UIButton}

/**
  * Created by tume on 8/9/16.
  */
class UILabelBuilder(private val label: UILabel = new UILabel()) extends UIBuilder(label) {

  def img(drawable: Int) : this.type = {
    uiComponent.asInstanceOf[UILabel].iconResource = drawable
    this
  }

  def text(text: String) : this.type = {
    uiComponent.asInstanceOf[UILabel].text = text
    this
  }

  def tSize(size: Int) : this.type = {
    uiComponent.asInstanceOf[UILabel].textSize = size
    this
  }
}
