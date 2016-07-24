package com.tume.engine.model

import android.graphics.Canvas
import com.tume.engine.Game
import com.tume.engine.model.Border.Border
import com.tume.engine.util.Calc

/**
  * Created by tume on 7/7/16.
  */
abstract class SGObject {

  var x, y = 0F
  var xSpeed, ySpeed = 0F

  var dampening = 1f

  var mass = 1f

  def collisionGroup: Int = -1
  def collidesWith = Seq.empty[Int]

  def boundingBox : Shape

  def update(delta: Float) = {
    x += xSpeed * delta
    y += ySpeed * delta
    val damp = Calc.pow(dampening, delta)
    xSpeed *= damp
    ySpeed *= damp
  }

  def movementSpeed = Math.sqrt(xSpeed*xSpeed+ySpeed*ySpeed).toFloat
  def movementAngle : Float = Calc.atan2((xSpeed, ySpeed))

  def applyForce(force: (Float, Float)) = {
    this.xSpeed += force._1 / mass
    this.ySpeed += force._2 / mass
  }

  def render(c: Canvas)
  def isRemovable: Boolean

  def onCollision(another: SGObject) = { }
  def onPhysicsCollision(another: SGObject) = {}
  def onBorderCollision(borders: Map[Border, Boolean], bounds: Rect) = { }
  def onBorderExit(borders: Map[Border, Boolean], bounds: Rect) = { }

  def bounceFrom(borders: Map[Border, Boolean], bounds: Rect): Unit = {
    if ((borders(Border.Left) && xSpeed < 0) || (borders(Border.Right) && xSpeed > 0)) {
      xSpeed = -xSpeed
      if (borders(Border.Left)) x = bounds.left + boundingBox.width / 2
      if (borders(Border.Right)) x = bounds.right - boundingBox.width / 2
    }
    if ((borders(Border.Top) && ySpeed < 0) || (borders(Border.Bottom) && ySpeed > 0)) {
      ySpeed = -ySpeed
      if (borders(Border.Top)) y = bounds.top + boundingBox.height / 2
      if (borders(Border.Bottom)) y = bounds.bottom - boundingBox.height / 2
    }
  }

  def stopTo(borders: Map[Border, Boolean], bounds: Rect): Unit = {
    if ((borders(Border.Left) && xSpeed < 0) || (borders(Border.Right) && xSpeed > 0)) {
      xSpeed = 0
      if (borders(Border.Left)) x = bounds.left + boundingBox.width / 2
      if (borders(Border.Right)) x = bounds.right - boundingBox.width / 2
    }
    if ((borders(Border.Top) && ySpeed < 0) || (borders(Border.Bottom) && ySpeed > 0)) {
      ySpeed = 0
      if (borders(Border.Top)) y = bounds.top + boundingBox.height / 2
      if (borders(Border.Bottom)) y = bounds.bottom - boundingBox.height / 2
    }
  }

  def onAdd(game: Game) : Unit = { }

}
