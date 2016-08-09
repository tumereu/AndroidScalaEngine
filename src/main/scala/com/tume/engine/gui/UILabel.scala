package com.tume.engine.gui

import android.graphics.{Bitmap, Rect, Canvas}
import com.tume.engine.util.{Calc, Bitmaps, DisplayUtils}

/**
  * Created by tume on 8/9/16.
  */
class UILabel extends UIComponent {

  var text = ""
  var iconResource = -1
  var textSize = -1
  protected var icon: Option[Bitmap] = None

  override def render(canvas: Canvas): Unit = {
    var iconWidth = 0f
    if (icon.isDefined) {
      val b = icon.get
      val scale = Calc.min(1f, Calc.min(width / b.getWidth.toFloat, height / b.getHeight.toFloat))
      canvas.save()
      canvas.translate(b.getWidth * scale / 2 + DisplayUtils.scale, height / 2)
      canvas.scale(scale, scale)
      canvas.drawBitmap(b, -b.getWidth / 2, -b.getHeight / 2, UITheme.bitmapPaint)
      canvas.restore()
      iconWidth = b.getWidth * scale + DisplayUtils.scale * 3
    }
    iconWidth += DisplayUtils.scale * 1
    if (text.length > 0) {
      val p = UITheme.labelPaint
      p.setTextSize(if (textSize <= 0) height * 0.7f - DisplayUtils.scale * 2 else textSize)
      val bounds = new Rect()
      p.getTextBounds(text, 0, text.length, bounds)
      canvas.drawText(text, iconWidth, height / 2 + bounds.height() / 2, p)
    }
  }

  override def init(): Unit = {
    super.init()
    icon = iconResource match {
      case -1 => None
      case x => Some(Bitmaps.get(x))
    }
  }
}