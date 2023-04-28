package com.hcy.androidlocalizationbyform.translation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class ChooseXml : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        var data = e.getData(CommonDataKeys.VIRTUAL_FILE)
        print(data)
    }
}
