package com.tume.engine.gui

import android.graphics.{Rect, Paint, Bitmap, Canvas}
import android.util.Log
import com.tume.engine.gui.event.ButtonEvent
import com.tume.engine.gui.model.{ButtonModel, UIModel}
import com.tume.engine.util.{Calc, DisplayUtils, Bitmaps}

/**
  * Created by tume on 5/12/16.
  */
class UIButton extends UIComponent {

  import com.tume.engine.gui.UIState._

  var rightCornerText = Option[(String, Int)](null)
  var leftCornerText = Option[(String, Int)](null)
  var imageResource = -1
  var bgColor: Option[Int] = None
  var instant = false
  protected var image: Option[Bitmap] = None

  override def render(canvas: Canvas): Unit = {
    val fillPaint = this.state match {
      case Pressed => UITheme.fillPaintPressed
      case Disabled => UITheme.fillPaintDisabled
      case _ if bgColor.isDefined => { val p = new Paint(); p.setColor(bgColor.get); p.setStyle(Paint.Style.FILL); p}
      case _ => UITheme.shade(UITheme.fillPaintButtonTop.getColor, UITheme.fillPaintButtonDown.getColor, this)
    }
    val borderPaint = state match {
      case Disabled => UITheme.borderPaintDisabled
      case _ => UITheme.borderPaint
    }
    val b = DisplayUtils.scale * 2
    canvas.drawRoundRect(0, 0, width, height, UITheme.cornerRadius, UITheme.cornerRadius, borderPaint)
    canvas.drawRoundRect(b, b, width - b, height - b, UITheme.cornerRadius, UITheme.cornerRadius, fillPaint)
    if (image.isDefined) {
      val bitmap = image.get
      val scale = Calc.min(1f, Calc.min((width - b * 2) / bitmap.getWidth.toFloat, (height - b * 2) / bitmap.getHeight.toFloat))
      canvas.save()
      canvas.translate(width / 2, height / 2)
      canvas.scale(scale, scale)
      canvas.drawBitmap(bitmap, -bitmap.getWidth / 2, -bitmap.getHeight / 2, UITheme.bitmapPaint)
      canvas.restore()
    }
    if (rightCornerText.isDefined) {
      val p = UITheme.textPaint(rightCornerText.get._2)
      p.setTextSize(DisplayUtils.scale * 10)
      val bounds = new Rect()
      p.getTextBounds(rightCornerText.get._1, 0, rightCornerText.get._1.length, bounds)
      canvas.drawText(rightCornerText.get._1, width - b * 3 - bounds.width(), b * 3 + bounds.height(), p)
    }
    if (leftCornerText.isDefined) {
      val p = UITheme.textPaint(leftCornerText.get._2)
      p.setTextSize(DisplayUtils.scale * 10)
      val bounds = new Rect()
      p.getTextBounds(leftCornerText.get._1, 0, leftCornerText.get._1.length, bounds)
      canvas.drawText(leftCornerText.get._1, b * 3, b * 3 + bounds.height(), p)
    }
  }

  def onPress() : Unit = {
    throwEvent(ButtonEvent())
  }

  override def onTouch(): Unit = if (instant) onPress()
  override def onClick(): Unit = if (!instant) onPress()

  override def init(): Unit = {
    super.init()
    loadImage()
  }

  override def register(uIModel: UIModel): Unit = {
    super.register(uIModel)
    if (uIModel.icon.isDefined) {
      this.imageResource = uIModel.icon.get
    } else {
      this.imageResource = -1
    }
    this.bgColor = uIModel.bgColor
    uIModel match {
      case b : ButtonModel => {
        leftCornerText = b.leftCornerText
        rightCornerText = b.rightCornerText
      }
      case _ => rightCornerText = None; leftCornerText = None
    }
    loadImage()
  }

  def loadImage(): Unit = {
    image = imageResource match {
      case -1 => None
      case x => Some(Bitmaps.get(x))
    }
  }
}
