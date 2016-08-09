package com.tume.engine.gui

import android.graphics._
import com.tume.engine.Input
import com.tume.engine.gui.event.{UIEvent, UIEventListener}
import com.tume.engine.model.{Vec2, Shape, Rect}
import com.tume.engine.util.{Bitmaps, DisplayUtils}

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

  var enabled = true
  var visible = true

  var x, y, width, height = 0

  def render(canvas: Canvas) : Unit
  def init(): Unit = {}

  def state : UIState = {
    if (!visible) {
      Hidden
    } else if (!enabled) {
      Disabled
    } else {
      if (this.innerState == Normal) {
        if (UIFocus.currentFocus.contains(this)) {
          Focused
        } else {
          Normal
        }
      } else {
        innerState
      }
    }
  }


  def toggleVisibility(boolean: Boolean): Unit = {
    this.visible = boolean
  }

  def onViewShow(): Unit = {
    toggleVisibility(true)
  }

  def toggleEnabled(boolean: Boolean): Unit = {
    this.enabled = boolean
  }

  def interactable = visible && enabled && uiSystem.isReceivingInput(this)

  def update(delta: Float): Unit = {
    if (Input.tap(boundingBox) && visible) {
      onClick()
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

  val fillPaintNormal = create(0xffAA6600, Paint.Style.FILL)
  val fillPaintNormalBright = create(0xffCC9933, Paint.Style.FILL)
  val fillPaintDisabled = create(0xff898989, Paint.Style.FILL)
  val fillPaintPressed = create(0xff996699, Paint.Style.FILL)
  val fillPaintPanel = create(0xff606060, Paint.Style.FILL)

  val textPaint = create(0xffaaceaa)
  val labelPaint = create(0xff000000)

  val background = create(0xff666666, Paint.Style.FILL)

  private def create(color: Int, style: Paint.Style = Paint.Style.FILL_AND_STROKE, strokeWidth: Float = DisplayUtils.scale, antialias: Boolean = true) : Paint = {
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
