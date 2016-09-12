package com.tume.engine.gui.model


/**
  * Created by tume on 8/12/16.
  */
trait UIModel {

  def tooltip = Option[String](null)
  def icon : Option[Int] = None
  def bgColor: Option[Int] = None

}
trait ButtonModel {

  def leftCornerText = Option[(String, Int)](null)
  def rightCornerText = Option[(String, Int)](null)

}
