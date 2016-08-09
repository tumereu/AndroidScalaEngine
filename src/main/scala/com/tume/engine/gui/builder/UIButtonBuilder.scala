package com.tume.engine.gui.builder

import com.tume.engine.gui.{UIInstantButton, UIButton}

/**
  * Created by tume on 8/8/16.
  */
class UIButtonBuilder(private val button: UIButton = new UIButton()) extends UIBuilder(button) {

  def img(drawable: Int) : this.type = {
    uiComponent.asInstanceOf[UIButton].imageResource = drawable
    this
  }
}
