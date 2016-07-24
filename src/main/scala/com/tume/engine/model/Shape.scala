package com.tume.engine.model

import com.tume.engine.util.Calc._

/**
  * Created by tume on 7/7/16.
  */
trait Shape {
  def width: Float
  def height: Float
  def contains(point: (Float, Float)) : Boolean
  def intersects(shape: Shape): Boolean
  def center: (Float, Float)
}
case class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float) extends Shape {
  override def width = right - left
  override def height = bottom - top

  override def contains(point: (Float, Float)): Boolean = {
    point._1 >= left && point._1 <= right && point._2 >= top && point._2 <= bottom
  }
  override def intersects(shape: Shape): Boolean = shape match {
    case Rect(l, t, r, b) => contains((l, t)) || contains((t, r)) || contains((r, b)) || contains(b, l)
    case c: Circle => c.contains((clamp(c.x, left, right), clamp(c.y, top, bottom)))
    case p: Point => p.intersects(this)
    case _ => ???
  }
  override def center = (left + width / 2, top + height / 2)
}
case class Circle(val x: Float, val y: Float, val radius: Float) extends Shape {
  override def width = radius * 2
  override def height = radius * 2
  override def contains(point: (Float, Float)): Boolean = {
    Math.sqrt((point._1-x)*(point._1-x)+(point._2-y)*(point._2-y)) <= radius
  }
  override def intersects(shape: Shape): Boolean = shape match {
    case rect: Rect => rect.intersects(this)
    case Circle(rx, ry, r) => Math.sqrt((rx-x)*(rx-x)+(ry-y)*(ry-y)) <= radius + r
    case p: Point => p.intersects(this)
    case _ => ???
  }
  override def center = (x, y)
}
case class Point(val x: Float, val y: Float) extends Shape {
  override def width: Float = 1f
  override def height: Float = 1f
  override def center: (Float, Float) = (x, y)
  override def contains(point: (Float, Float)): Boolean = point._1 == x && point._2 == y
  override def intersects(shape: Shape): Boolean = shape.contains((x, y))
}
