package com.tume.engine.gui

import com.tume.engine.gui.model.UIModel
import com.tume.engine.util.Calc

import scala.collection.mutable

/**
  * Created by tume on 8/27/16.
  */
class UISelectionPopupPanel extends UIPopupPanel {

  var models = Seq.empty[UIModel]
  var selectedPage = 0
  var buttonAmount = 0
  var maxPages = 0

  def register(seq: Seq[UIModel]): Unit = {
    this.models = seq
    var buttons = mutable.Buffer[UIButton]()
    var label = Option[UILabel](null)
    for (c <- components) {
      c match {
        case b : UIButton if b.id.get.contains("_button") => buttons += b
        case l : UILabel => label = Some(l)
        case _ =>
      }
    }

    buttonAmount = buttons.size
    maxPages = models.size / buttonAmount + (if (models.size % buttonAmount > 0) 1 else 0)
    if (selectedPage >= maxPages) {
      selectedPage = 0
    } else if (selectedPage < 0) {
      selectedPage = maxPages - 1
    }

    for (i <- buttons.indices) {
      val index = selectedPage * buttons.size + i
      if (models.isDefinedAt(index)) {
        buttons(i).register(models(index))
        buttons(i).enabled = true
      } else {
        buttons(i).unregisterModel()
        buttons(i).enabled = false
      }
    }
    label.foreach(_.text = "Page " + (selectedPage + 1) + "/" + Calc.min(maxPages, 1))
  }

  def nextPage(): Unit = {
    selectedPage += 1
    register(models)
  }

  def prevPage(): Unit = {
    selectedPage -= 1
    register(models)
  }

}
