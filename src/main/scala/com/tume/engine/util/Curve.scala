package com.tume.engine.util

import com.tume.engine.model.Vec2

/**
  * Created by tume on 5/15/16.
  */
abstract class Curve {
  def eval(t: Float): Vec2
}
case class LinearCurve(p0: Vec2, p1: Vec2) extends Curve {
  override def eval(t: Float): Vec2 = Vec2(p0.x + (p1.x - p0.x) * t, p0.y + (p1.y - p0.y) * t)
}
case class QuadraticCurve(val p0: Vec2, val p1: Vec2, val p2: Vec2) extends Curve {
  override def eval(t: Float): Vec2 = {
    val x = (1 - t) * (1 - t) * p0.x + 2 * (1 - t) * t * p1.x + t * t * p2.x
    val y = (1 - t) * (1 - t) * p0.y + 2 * (1 - t) * t * p1.y + t * t * p2.y
    Vec2(x.toFloat, y.toFloat)
  }
}