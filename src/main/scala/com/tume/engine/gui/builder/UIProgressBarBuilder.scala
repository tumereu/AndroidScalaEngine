package com.tume.engine.gui.builder

import com.tume.engine.gui.UIProgressBar

/**
  * Created by tume on 8/8/16.
  */
class UIProgressBarBuilder extends UIBuilder(new UIProgressBar()){

  def main(int: Int, int2: Int) : UIProgressBarBuilder = {
    uiComponent.asInstanceOf[UIProgressBar].mainColor = int
    uiComponent.asInstanceOf[UIProgressBar].mainColorBright = int2
    this
  }

  def tick(int: Int, int2: Int) : UIProgressBarBuilder = {
    uiComponent.asInstanceOf[UIProgressBar].tickColor = int
    uiComponent.asInstanceOf[UIProgressBar].tickColorBright = int2
    this
  }
}
