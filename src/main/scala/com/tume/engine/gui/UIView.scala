package com.tume.engine.gui

import com.tume.engine.gui.builder.UIBuilder

/**
  * Created by tume on 5/12/16.
  */
trait UIView {

  def name: String = getClass.getSimpleName
  def build : Seq[UIBuilder[_ <: UIComponent]]

  def bottom = UIBuilder(new UISpace()).bottom()
  def top = UIBuilder(new UISpace()).top()
  def left = UIBuilder(new UISpace()).left()
  def right = UIBuilder(new UISpace()).right()
}
