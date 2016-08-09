package com.tume.engine.gui

import android.graphics.{Rect, Canvas}
import com.tume.engine.util.DisplayUtils

/**
  * Created by tume on 8/9/16.
  */
class UIPanel extends UIComponent {

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
    canvas.drawRoundRect(b, b, width - b, height - b, UITheme.cornerRadius, UITheme.cornerRadius, fillPaint)
  }
}
