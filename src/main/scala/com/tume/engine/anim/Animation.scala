package com.tume.engine.anim

import com.tume.engine.anim.LoopType.LoopType
import com.tume.engine.util.{L, Calc, Timer}

/**
  * Created by tume on 8/1/16.
  */
abstract class Animation(private[anim] val duration: Float, private[anim] val loopType: LoopType) {

  var onFinish = () => {}

  val id = java.util.UUID.randomUUID().hashCode()

  Animations.register(this)
  import com.tume.engine.anim.LoopType._

  protected var counter : Float = 0f

  protected var finished = false
  protected var direction = 1f
  protected var paused = false
  protected var timer = Option[Timer](null)

  private var reversed = false

  private[anim] def update(delta: Float): Unit = {
    counter += direction * delta / duration
    if (counter > 1 || counter < 0) {
      counter = Calc.clamp(counter, 0, 1)
      animationFinished()
    }
    if (timer.isDefined && timer.get.tick(delta)) {
      finished = true
    }
  }

  def pause() : Unit = paused = true
  def resume() : Unit = paused = false
  def reset() : Unit = {
    finished = false
    paused = false
    counter = 0f
    timer.foreach(_.reset())
    Animations.register(this)
  }

  override def hashCode = id
  override def equals(o: Any) : Boolean = o match {
    case animation: Animation => animation.id == this.id
    case _ => false
  }

  private def animationFinished(): Unit = {
    loopType match {
      case Once => finished = true
      case Repeat => counter = 0f
      case Reverse => direction *= -1f
    }
    onFinish()
  }

  protected def t = counter
  private[anim] def v: Float

  def value : Float = if (reversed) 1f - v else v
  def value(f: Float) : Float = value * f
  def value(i: Int): Int = (value * i).toInt
  def reverse: Animation = {
    this.reversed = true
    this
  }
  def isFinished = finished

  private[anim] def isRemovable = finished

}
object Animation {
  def apply(): Animation = EmptyAnim()
}
object Animations {
  protected var anims = Set.empty[Animation]

  def update(delta: Float): Unit = {
    anims.foreach(_.update(delta))
    anims = anims.filterNot(_.isRemovable)
  }

  def register(animation: Animation): Unit = {
    anims = anims + animation
  }
}
object LoopType extends Enumeration {
  type LoopType = Value
  val Repeat, Reverse, Once = Value
}
case class EmptyAnim() extends Animation(1, LoopType.Once) {
  override def v = 1f
  override def isRemovable = true
}
case class LinearAnim(dur: Float, lt: LoopType = LoopType.Once) extends Animation(dur, lt) {
  override def v = t
}
case class ClampedLinearSpikeAnim(dur: Float, fac: Float, lt: LoopType = LoopType.Once) extends Animation(dur, lt) {
  override def v = {
    if (t < 0.5f) Calc.min(t * 2 * fac, 1f)
    else Calc.min((1f - (t - 0.5f) * 2) * fac, 1f)
  }
}
case class QuinticOutAnim(dur: Float, lt: LoopType = LoopType.Once) extends Animation(dur, lt) {
  override def v = Calc.pow(t - 1f, 5f) + 1
}
