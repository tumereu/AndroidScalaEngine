package com.tume.engine.gui

import android.graphics
import android.graphics._
import com.tume.engine.Input
import com.tume.engine.gui.event.{UIEvent, UIEventListener}
import com.tume.engine.gui.model.UIModel
import com.tume.engine.model.{RichString, Vec2, Shape, Rect}
import com.tume.engine.util._

/**
  * Created by tume on 5/12/16.
  */
abstract class UIComponent {
  import com.tume.engine.gui.UIState._
  protected var innerState = Normal

  var id : Option[String] = None
  var view: Option[String] = None
  var listener: Option[UIEventListener] = None
  var uiSystem: UISystem = null

  var tooltip = Option[String](null)
  def tooltipWidth = DisplayUtils.screenWidth * 2 / 3

  var enabled = true
  var visible = true
  private var tooltipRequested = 0
  def shouldRenderTooltip = tooltipRequested > 0 && tooltip.isDefined

  var x, y, width, height = 0

  def render(canvas: Canvas) : Unit
  def init(): Unit = {}

  def renderTooltip(canvas: Canvas): Unit = {
    val breaked = LineBreak.break(tooltip.get, 30)
    var w = 0f
    var h = DisplayUtils.scale * 9
    val th = breaked.foldLeft(0)((n: Int, s: String) => {
      val bounds = new graphics.Rect()
      UITheme.tooltipTextPaint.getTextBounds(s, 0, s.length, bounds)
      Calc.max(n, bounds.height())
    })
    for (s2 <- breaked) {
      val bounds = new graphics.Rect()
      UITheme.tooltipTextPaint.getTextBounds(s2, 0, s2.length, bounds)
      w = Calc.max(bounds.width(), w)
      h += th + DisplayUtils.scale
    }

    w += DisplayUtils.scale * 16

    val b = Bitmap.createBitmap(w.toInt + 1, h.toInt + 1, Bitmap.Config.ARGB_8888)
    val c = new Canvas(b)

    var topLeft = Vec2(x + width / 2, y + height / 2)
    if (topLeft.x + w > DisplayUtils.screenWidth) {
      topLeft -= Vec2(topLeft.x + w - DisplayUtils.screenWidth, 0)
    }
    if (topLeft.y + h > DisplayUtils.screenHeight) {
      topLeft -= Vec2(0, topLeft.y + h - DisplayUtils.screenHeight)
    }
    val os = DisplayUtils.scale * 2
    c.drawRoundRect(0, 0, w, h, UITheme.cornerRadius, UITheme.cornerRadius, UITheme.tooltipBorderPaint)
    c.drawRoundRect(os, os, w - os, h - os, UITheme.cornerRadius, UITheme.cornerRadius, UITheme.tooltipFillPaint)
    var counter = 1
    for (s <- breaked) {
      c.drawText(s, DisplayUtils.scale * 7, DisplayUtils.scale * (counter + 2) + th * counter, UITheme.tooltipTextPaint)
      counter += 1
    }

    canvas.drawBitmap(b, topLeft.x, topLeft.y, UITheme.tooltipBitmapPaint)
  }

  def state : UIState = {
    if (!visible) {
      tooltipRequested = 0
      Hidden
    } else if (!enabled) {
      tooltipRequested = 0
      Disabled
    } else {
      if (this.innerState == Normal) {
        if (UIFocus.currentFocus.contains(this)) {
          Focused
        } else {
          tooltipRequested = 0
          Normal
        }
      } else {
        innerState
      }
    }
  }

  def register(uIModel: UIModel): Unit = {
    this.tooltip = uIModel.tooltip
  }

  def unregisterModel(): Unit = {
    this.register(new UIModel {})
  }

  def toggleVisibility(boolean: Boolean): Unit = {
    this.visible = boolean
    this.tooltipRequested = 0
  }

  def onViewShow(): Unit = {
    toggleVisibility(true)
  }

  def toggleEnabled(boolean: Boolean): Unit = {
    this.enabled = boolean
  }

  def interactable = visible && enabled && uiSystem.isReceivingInput(this)

  def update(delta: Float, onTop: Boolean): Unit = {
    if (onTop && enabled) {
      if (Input.tap(boundingBox) && visible && innerState == Pressed) {
        if (tooltipRequested <= 1) { onClick(); tooltipRequested = 0 }
        else tooltipRequested -= 1
      }
      if (Input.touching(boundingBox) && visible) {
        this.innerState = Pressed
        UIFocus.currentFocus = Some(this)
        if (Input.touchStarted(boundingBox)) {
          onTouch()
        }
      } else {
        this.innerState = Normal
      }
      if (Input.longPress(boundingBox)) {
        tooltipRequested = if (tooltip.isEmpty) 0 else 2
      }
    }
  }

  def onTouch(): Unit = {}
  def onClick(): Unit = {}

  def throwEvent(event: UIEvent): Unit = {
    if (this.listener.isDefined && this.view.isDefined && this.id.isDefined) {
      event.view = this.view
      event.id = this.id
      this.listener.get.onUIEvent(event)
    }
  }

  def boundingBox : Shape = Rect(x, y, x + width, y + height)

  def layer = 0

  def contains(uIComponent: UIComponent) : Boolean = this == uIComponent

  def onAddAsPopup(activePopups: Vector[UIComponent]): Unit = {}
  def onRemoveAsPopup(activePopups: Vector[UIComponent]): Unit = {}

  def find(id: String): Option[UIComponent] = {
    if (this.id.contains(id)) {
      Some(this)
    } else {
      None
    }
  }

}
object UIState extends Enumeration {
  type UIState = Value
  val Normal, Disabled, Hidden, Focused, Pressed = Value
}
object UIFocus {
  var currentFocus: Option[UIComponent] = None
}
object UITheme {
  val cornerRadius = DisplayUtils.scale * 10

  val bitmapPaint = new Paint()

  val borderPaint = create(0xff999999, Paint.Style.FILL, 2 * DisplayUtils.scale)
  val borderPaintDisabled = create(0xff454545, Paint.Style.FILL, 2 * DisplayUtils.scale)
  val borderPaintPanel = create(0xff339999, Paint.Style.FILL)

  val fillPaintButtonDown = create(0xffAA6600, Paint.Style.FILL)
  val fillPaintButtonTop = create(0xffCC9933, Paint.Style.FILL)
  val fillPaintDisabled = create(0xff898989, Paint.Style.FILL)
  val fillPaintPressed = create(0xff996699, Paint.Style.FILL)

  val fillPaintPanel = create(0xff606060, Paint.Style.FILL)

  val tooltipBorderPaint = create(0xffffffff, Paint.Style.FILL)
  val tooltipFillPaint = create(0xff555555, Paint.Style.FILL)
  val tooltipTextPaint = { val p=create(0xffffffff, strokeWidth = 2); p.setTextSize(DisplayUtils.scale * 15); p }
  val tooltipBitmapPaint = { val p = create(0xffffffff, strokeWidth = 2); p.setAlpha(200); p}

  val textPaint = create(0xffaaceaa)
  val labelPaint = create(0xff000000)

  def textPaint(color: Int) = create(color)

  val background = create(0xff666666, Paint.Style.FILL)

  def create(color: Int, style: Paint.Style = Paint.Style.FILL_AND_STROKE, strokeWidth: Float = DisplayUtils.scale, antialias: Boolean = true) : Paint = {
    val paint = new Paint()
    paint.setColor(color)
    paint.setStyle(style)
    paint.setStrokeWidth(strokeWidth)
    paint.setAntiAlias(antialias)
    paint
  }

  def shade(topColor: Int, botColor: Int, topLeft: Vec2, botRight: Vec2): Paint = {
    val p = create(0xFFFFFFFF, Paint.Style.FILL)
    p.setShader(new LinearGradient(topLeft.x, topLeft.y, topLeft.x, botRight.y, topColor, botColor, Shader.TileMode.MIRROR))
    p
  }

  def shade(topColor: Int, botColor: Int, u: UIComponent): Paint = {
    shade(topColor, botColor, Vec2(0, 0), Vec2(0, u.height))
  }
}
