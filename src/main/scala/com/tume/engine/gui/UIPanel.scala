package com.tume.engine.gui

import android.graphics._
import com.tume.engine.gui.model.UIModel
import com.tume.engine.util.{Calc, Bitmaps, DisplayUtils}

/**
  * Created by tume on 8/9/16.
  */
class UIPanel extends UIComponent {

  var imageResource = -1
  protected var image: Option[Bitmap] = None

  import com.tume.engine.gui.UIState._

  override def render(canvas: Canvas): Unit = {
    val fillPaint = this.state match {
      case Disabled => UITheme.fillPaintDisabled
      case _ => UITheme.fillPaintNormalBright
    }
    val borderPaint = state match {
      case Disabled => UITheme.borderPaintDisabled
      case _ => UITheme.borderPaint
    }
    val b = DisplayUtils.scale * 2
    canvas.drawRoundRect(0, 0, width, height, UITheme.cornerRadius, UITheme.cornerRadius, borderPaint)
    if (image.isDefined) {
      val path = new Path()
      path.addRoundRect(new RectF(b, b, width -b, height -b), UITheme.cornerRadius, UITheme.cornerRadius, Path.Direction.CW)
      canvas.clipPath(path)
      val bitmap = image.get
      val scale = Calc.min(1f, Calc.max((width - b * 2) / bitmap.getWidth.toFloat, (height - b * 2) / bitmap.getHeight.toFloat))
      canvas.save()
      canvas.translate(width / 2, height / 2)
      canvas.scale(scale, scale)
      canvas.drawBitmap(bitmap, -bitmap.getWidth / 2, -bitmap.getHeight / 2, UITheme.bitmapPaint)
      canvas.restore()
    } else {
      canvas.drawRoundRect(b, b, width - b, height - b, UITheme.cornerRadius, UITheme.cornerRadius, fillPaint)
    }
  }

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
    loadImage()
  }

  def loadImage(): Unit = {
    image = imageResource match {
      case -1 => None
      case x: Int => Some(Bitmaps.get(x))
    }
  }
}
