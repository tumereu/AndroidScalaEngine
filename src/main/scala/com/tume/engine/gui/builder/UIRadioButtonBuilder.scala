package com.tume.engine.gui.builder

import com.tume.engine.gui.UIRadioButton

/**
  * Created by tume on 8/27/16.
  */
class UIRadioButtonBuilder extends UIButtonBuilder[UIRadioButton](new UIRadioButton()) {

  var group = ""
  def group(s: String): this.type = {
    this.group = s
    this
  }

  def link(b: UIRadioButtonBuilder): Unit = {
    val but = b.resolve
    val mine = resolve
    if (!but.groupedWith.contains(mine)) {
      but.groupedWith = but.groupedWith :+ mine
    }
    if (!mine.groupedWith.contains(but)) {
      mine.groupedWith = mine.groupedWith :+ but
    }
  }
}
