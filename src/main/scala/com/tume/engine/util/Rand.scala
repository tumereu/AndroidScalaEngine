package com.tume.engine.util

import com.tume.engine.model.{Rect, Vec2}

/**
  * Created by tume on 8/8/16.
  */
object Rand {

  def f: Float = Math.random().toFloat
  def f(factor: Float): Float = f * factor
  def f(min: Float, max: Float): Float = f(max - min) + min

  def i(fac: Int) : Int = (f * fac).toInt
  def i(min: Int, max: Int): Int = i(max - min + 1) + min

  def from[T](seq: Seq[T]): T = seq(i(seq.size))

  def angle = f(Calc.PI * 2)

  def vec(left: Float, top: Float, right: Float, bottom: Float) : Vec2 = Vec2(f(left, right), f(top, bottom))
  def vec(maxX: Float, maxY: Float) : Vec2 = vec(0, maxX, 0, maxY)
  def vec(rect: Rect): Vec2 = vec(rect.left, rect.top, rect.right, rect.bottom)

  def circle : Vec2 = {
    val dir = Calc.dir(Rand.angle)
    Vec2(dir._1, dir._2)
  }

}
