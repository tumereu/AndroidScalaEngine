package com.tume.engine

import android.graphics.{Paint, Canvas}
import android.view.View
import com.tume.engine.effect.{RenderableEffect, EffectSystem}
import com.tume.engine.gui.model.UIModel
import com.tume.engine.gui._
import com.tume.engine.gui.event.{UIEvent, UIEventListener}
import com.tume.engine.model.Border.Border
import com.tume.engine.model.Border.Border
import com.tume.engine.model._
import com.tume.engine.util.{L, DisplayUtils}

import scala.collection.immutable.HashMap

/**
  * Created by tume on 5/12/16.
  */
trait Game extends UIEventListener {

  private var idCounter = 0L

  var uiSystem : UISystem = null
  var effectSystem: EffectSystem = null

  var gameView: GameView = null

  var background : Background = ColorBackground(0xFF000000)

  def gameBounds: Option[Rect] = Some(Rect(0, 0, DisplayUtils.screenWidth, DisplayUtils.screenHeight))

  var objects = Vector.empty[SGObject]

  def update(delta: Float) = {
    this.background.update(delta)
    // Update the objects and check border collisions
    for (o <- objects) {
      o.update(delta)
      if (gameBounds.isDefined) {
        val bb = o.boundingBox
        var borders = HashMap[Border, Boolean]()
        borders += Border.Left -> (o.loc.x - bb.width / 2 < gameBounds.get.left)
        borders += Border.Right -> (o.loc.x + bb.width / 2 > gameBounds.get.right)
        borders += Border.Top -> (o.loc.y - bb.height / 2 < gameBounds.get.top)
        borders += Border.Bottom -> (o.loc.y + bb.height / 2 > gameBounds.get.bottom)
        if (borders.values.exists(b => b)) {
          o.onBorderCollision(borders, gameBounds.get)
          borders = HashMap[Border, Boolean]()
          borders += Border.Left -> (o.loc.x + bb.width / 2 < gameBounds.get.left)
          borders += Border.Right -> (o.loc.x - bb.width / 2 > gameBounds.get.right)
          borders += Border.Top -> (o.loc.y + bb.height / 2 < gameBounds.get.top)
          borders += Border.Bottom -> (o.loc.y - bb.height / 2 > gameBounds.get.bottom)
          if (borders.values.exists(b => b)) {
            o.onBorderExit(borders, gameBounds.get)
          }
        }
      }
    }
    // Object to object collisions
    for (i <- 0 until objects.size - 1) {
      val objectI = objects(i)
      for (j <- i + 1 until objects.size) {
        val objectJ = objects(j)
        val iCollides = objectI.collidesWith.contains(objectJ.collisionGroup)
        val jCollides = objectJ.collidesWith.contains(objectI.collisionGroup)
        if ((iCollides || jCollides) && objectI.boundingBox.intersects(objectJ.boundingBox)) {
          if (iCollides) objectI.onCollision(objectJ)
          if (jCollides) objectJ.onCollision(objectI)
          objectI.onPhysicsCollision(objectJ)
        }
      }
    }
    objects = objects.filterNot(_.isRemovable)
  }

  def render(canvas: Canvas) = {
    for (o <- objects) {
      canvas.save()
      canvas.translate(o.loc.x, o.loc.y)
      o.render(canvas)
      canvas.restore()
    }
  }

  def addObject(sGObject: SGObject): Unit = {
    this.objects = this.objects :+ sGObject
    sGObject.onAdd(this)
  }

  def addEffect(renderableEffect: RenderableEffect): Unit = {
    effectSystem.add(renderableEffect)
  }

  def init()

  def views: Seq[UIView]

  def onUIEvent(event: UIEvent): Unit = {
    uiSystem.activePopups.collect{ case a : UISelectionPopupPanel if a.id.isDefined => a }.foreach(p => {
      event.id match {
        case Some(s) => {
          val start = p.id.get + "_button"
          if (s.startsWith(start)) {
            val i = s.drop(start.length).toInt + p.selectedPage * p.buttonAmount
            if (onSelection(p.id.get, p.models(i), i)) {
              p.onToBeRemoved()
            }
          } else if (s.contains("_page_")) {
            if (s.endsWith("left")) {
              p.prevPage()
            } else {
              p.nextPage()
            }
          }
        }
        case None =>
      }
    })
  }

  def onSelection(id: String, uIModel: UIModel, index: Int): Boolean = true

  def showSelectionPopup(id: String, seq: UIModel): Unit = {
    val panel = findUIComponent[UISelectionPopupPanel](id)
    panel.register(seq)
    uiSystem.addPopup(panel)
  }

  def findUIComponent[T <: UIComponent](id: String) = uiSystem.findComponent[T](id)

  def uniqueId: Long = { this.idCounter += 1; this.idCounter }

  def showSelectionDialog(id: String, seq: Seq[UIModel]): Unit = {
    val panel = findUIComponent[UISelectionPopupPanel](id)
    panel.register(seq)
    uiSystem.addPopup(panel)
  }

}
