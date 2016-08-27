package com.tume.engine.gui.builder

import com.tume.engine.gui.{UIComponent, UIPopupPanel}

/**
  * Created by tume on 8/27/16.
  */
class UIPopupPanelBuilder(pup: UIPopupPanel = new UIPopupPanel()) extends UIBuilder(pup) {

  def += (uIBuilder: UIBuilder[_ <: UIComponent]) : this.type = {
    this.uiComponent += uIBuilder.uiComponent
    this
  }

}
