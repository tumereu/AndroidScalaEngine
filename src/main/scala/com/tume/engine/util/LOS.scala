package com.tume.engine.util

import com.tume.engine.model.Vec2

import scala.collection.mutable

/**
  * Created by tume on 9/11/16.
  */
object Los {

  def isVisible(fromX: Int, fromY: Int, toX: Int, toY: Int, losMap: LosMap) : Boolean = {
    if (fromX == toX && fromY == toY) {
      true
    } else {
      val line = raycast(fromX, fromY, toX, toY)
      var visible = true
      for ((x, y) <- line) {
        if ((x != fromX || y != fromX) && (x != toX || y != toY)) {
          visible = visible && losMap.transparent(x, y)
        }
      }
      visible
    }
  }

  def map(fromX: Int, fromY: Int, losMap: LosMap) : mutable.Map[(Int, Int), Boolean] = {
    val map = mutable.Map[(Int, Int), Boolean]()
    for (x <- 0 until losMap.width; y <- 0 until losMap.height) {
      map((x, y)) = isVisible(fromX, fromY, x, y, losMap)
    }
    map
  }

  def raycast(x0: Int, y0: Int, x1: Int, y1: Int) : Set[(Int, Int)] = {
    val stepMult = 0.1f
    val dx = x1 - x0
    val dy = y1 - y0
    val len = Vec2(dx, dy).len
    val sx = dx * stepMult / len
    val sy = dy * stepMult / len
    val steps = (len / stepMult).toInt
    var set = Set[(Int, Int)]()
    for (i <- 0 until steps) {
      val x = (0.5f + x0 + i * sx).toInt
      val y = (0.5f + y0 + i * sy).toInt
      set = set + ((x, y))
    }
    set
  }

}
trait LosMap {
  def width : Int
  def height: Int
  def transparent(x: Int, y: Int): Boolean
}
