package com.tume.engine.effect

import android.graphics.{Paint, Canvas}
import com.tume.engine.model.SGObject
import com.tume.engine.util.Calc

/**
  * Created by tume on 7/25/16.
  */
class CrossHair(sGObject: SGObject) extends RenderableEffect {
  var target: Option[SGObject] = Some(sGObject)

  var color = 0xFFFF0000
  var thickness = 4f
  var rad = 55f

  var targetLoc = sGObject.loc
  var startLoc = sGObject.loc
  var homingTime = 0f
  def currentLoc = startLoc.lerp(targetLoc, homingTime)

  var radiusScale = 1f

  var rotation = 0f
  var rotSpeed = Calc.PI / 4

  // If the crosshair should persist in added objects and receive updates even though it would otherwise be removed
  var persist = false

  override def update(delta: Float): Unit = {
    rotation += rotSpeed * delta
    if (target.isDefined && target.get.isRemovable) {
      target = None
    }
    if (target.isDefined) {
      targetLoc = target.get.loc
    } else {
      radiusScale -= delta * 5
    }
    homingTime = Calc.clamp(homingTime + delta * 4, 0f, 1f)
  }

  def radius = rad * radiusScale
  def reset(newTarget: SGObject): Unit = {
    startLoc = currentLoc
    homingTime = 0f
    if (target.isEmpty) {
      homingTime = 1f
    }
    target = Option(newTarget)
    radiusScale = 1f
  }

  override def render(canvas: Canvas): Unit = {
    if (radiusScale > 0) {
      val ls = radius * 0.35f
      val p = new Paint()
      p.setColor(color)
      p.setStrokeWidth(thickness)
      p.setStyle(Paint.Style.STROKE)
      canvas.save()
      canvas.translate(currentLoc.x, currentLoc.y)
      canvas.rotate(Calc.toDegrees(rotation))
      canvas.drawCircle(0, 0, radius, p)
      canvas.drawLine(-radius - ls, 0, -radius + ls, 0, p)
      canvas.drawLine(radius - ls, 0, radius + ls, 0, p)
      canvas.drawLine(0, -radius - ls, 0, -radius + ls, p)
      canvas.drawLine(0, radius - ls, 0, radius + ls, p)
      canvas.restore()
    }
  }

  override def isRemovable: Boolean = target.isEmpty && radiusScale < 0 && !persist
  override def layer = EffectLayer.BelowAll
}
