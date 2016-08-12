package com.tume.engine.gui.model

import android.graphics.Bitmap

/**
  * Created by tume on 8/12/16.
  */
trait UIModel {

  def tooltip = Option[String](null)
  def icon : Option[Int] = None

}
