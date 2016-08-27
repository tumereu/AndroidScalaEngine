package com.tume.engine.gui

import com.tume.engine.gui.UIState._

/**
  * Created by tume on 8/27/16.
  */
class UIRadioButton extends UIButton {

  var toggled = false

  var groupedWith = Vector.empty[UIRadioButton]

  override def onPress(): Unit = {
    super.onPress()
    toggle()
  }

  def toggle(): Unit = {
    groupedWith.foreach(_.toggled = false)
    this.toggled = true
  }

  override def state : UIState = {
    if (this.toggled) Disabled
    else super.state
  }

}
