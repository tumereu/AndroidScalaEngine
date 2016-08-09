package com.tume.engine.model

/**
  * Created by tume on 8/8/16.
  */
case class RichString(val value: String, val color: Int) {
  implicit class RichStringFunc(r: RichString) {
    def +(v: Vector[RichString]) = r + v
    def +(s: String) = r + s
  }
  def +(richString: RichString): Vector[RichString] = Vector(this, richString)
  def +(s: String): Vector[RichString] = Vector(this, RichString(s, color))
  def +(v: Vector[RichString]) = v :+ this
}
object RichString {
  def apply() = Vector.empty[RichString]
}