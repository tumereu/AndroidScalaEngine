package com.tume.engine.util

/**
  * Created by tume on 8/9/16.
  */
object LineBreak {
  def break(string: String, max: Int) : Vector[String] = {
    var b = Vector.newBuilder[String]
    val split = string.split(" ")
    var temp = ""
    for (s2 <- split) {
      var s = s2
      while (s.startsWith("\n")) {
        b += temp
        temp = ""
        s = s.tail
      }
      var lineBreaksAfter = 0
      while (s.endsWith("\n")) {
        s = s.dropRight(1)
        lineBreaksAfter += 1
      }
      if (temp.length() + s.length() + 1 > max) {
        b += temp
        temp = ""
      }
      if (temp.length > 0) {
        temp += " "
      }
      temp += s

      for (i <- 0 until lineBreaksAfter) {
        b += temp
        temp = ""
      }
    }
    if (temp.length > 0) {
      b += temp
    }
    b.result()
  }

}
