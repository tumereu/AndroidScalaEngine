package com.tume.engine.util

/**
  * Created by tume on 7/7/16.
  */
object Calc {
  def PI = Math.PI.toFloat

  def clamp(t: Float, min: Float, max: Float): Float = if (t > max) max else if (t < min) min else t

  def rand: Float = Math.random().toFloat
  def rand(factor: Float): Float = rand * factor
  def rand(min: Float, max: Float): Float = rand(max - min) + min
  def rand[T](seq: Seq[T]): T = seq(rand(seq.size).toInt)

  def randAngle = rand(PI * 2)

  def sin(angle: Float) = Math.sin(angle).toFloat
  def cos(angle: Float) = Math.cos(angle).toFloat

  def dir(angle: Float) : (Float, Float) = (sin(angle), cos(angle))
  def atan2(dir: (Float, Float)): Float = Math.atan2(dir._1, dir._2).toFloat
  def angle(dir: (Float, Float)): Float = Math.atan2(dir._2, dir._1).toFloat

  def pow(x: Float, exp: Float) = Math.pow(x, exp).toFloat

  def mult(tuple: (Float, Float), factor: Float) = (tuple._1 * factor, tuple._2 * factor)

  def abs(f: Float) = if (f < 0) -f else f
  def sqrt(f: Float) = Math.sqrt(f).toFloat

  def toDegrees(f: Float) = f * 180 / PI
  def toRadians(f: Float) = f * PI / 180

  def dist(p1: (Float, Float), p2: (Float, Float)) : Float = {
    val x = p1._1 - p2._1
    val y = p1._2 - p2._2
    sqrt(x*x+y*y)
  }

  def normalise(t: (Float, Float)) : (Float, Float) = {
    val l = Math.sqrt(t._1*t._1+t._2*t._2).toFloat
    (t._1 / l, t._2 / l)
  }
}
