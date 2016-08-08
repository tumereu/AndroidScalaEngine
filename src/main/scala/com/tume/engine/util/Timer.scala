package com.tume.engine.util

/**
  * Created by tume on 7/9/16.
  */
class Timer(val minTime: Float, val maxTime: Float = -1f) {
  var currentTime = -1f
  def tick(delta: Float): Boolean = {
    if (currentTime < 0) {
      reset()
      false
    } else {
      currentTime -= delta
      if (currentTime <= 0) {
        reset()
        true
      } else {
        false
      }
    }
  }
  def reset(): Unit = {
    currentTime = minTime
    if (maxTime > minTime) {
      currentTime += Rand.f(maxTime - minTime)
    }
  }
}
class Counter(val minTime: Int, val maxTime: Int = -1) {
  var currentTime = -1
  def increment(i: Int): Boolean = {
    if (currentTime < 0) {
      reset()
      false
    } else {
      currentTime -= i
      if (currentTime <= 0) {
        reset()
        true
      } else {
        false
      }
    }
  }
  def increment() : Boolean = increment(1)
  def reset(): Unit = {
    currentTime = minTime
    if (maxTime > minTime) {
      currentTime += Rand.i(maxTime - minTime + 1)
    }
  }
}
