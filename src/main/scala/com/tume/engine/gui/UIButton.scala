package com.tume.engine.gui

import android.graphics.{Rect, Paint, Bitmap, Canvas}
import android.util.Log
import com.tume.engine.gui.event.ButtonEvent
import com.tume.engine.gui.model.UIModel
import com.tume.engine.util.{Calc, DisplayUtils, Bitmaps}

/**
  * Created by tume on 5/12/16.
  */
class UIButton extends UIComponent {

  import com.tume.engine.gui.UIState._

  var cornerText = ""
  var imageResource = -1
  var bgColor: Option[Int] = None
  protected var image: Option[Bitmap] = None

  override def render(canvas: Canvas): Unit = {
    val fillPaint = this.state match {
      case Pressed => UITheme.fillPaintPressed
      case Disabled => UITheme.fillPaintDisabled
      case _ if bgColor.isDefined => { val p = new Paint(); p.setColor(bgColor.get); p.setStyle(Paint.Style.FILL); p}
      case _ => UITheme.shade(UITheme.fillPaintNormalBright.getColor, UITheme.fillPaintNormal.getColor, this)
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
    if (cornerText.length > 0) {
      val p = UITheme.textPaint
      p.setTextSize(DisplayUtils.scale * 15)
      val bounds = new Rect()
      p.getTextBounds(cornerText, 0, cornerText.length, bounds)
      canvas.drawText(cornerText, width - b * 3 - bounds.width(), b * 3 + bounds.height(), p)
    }
  }

  def onPress() : Unit = {
    throwEvent(ButtonEvent())
  }

  override def onClick(): Unit = onPress()

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
    loadImage()
  }

  def loadImage(): Unit = {
    image = imageResource match {
      case -1 => None
      case x => Some(Bitmaps.get(x))
    }
  }
}
