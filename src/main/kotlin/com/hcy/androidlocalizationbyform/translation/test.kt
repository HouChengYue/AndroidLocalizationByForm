package com.hcy.androidlocalizationbyform.translation

import com.intellij.build.events.BuildEventsNls.Message
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.Messages

class test : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
//        测试
//        e.getData(PlatformDataKeys.PROJECT)?.apply {
//            Messages.showMessageDialog(this, "test", "first Action", Messages.getInformationIcon())
//        }
//        选择文件
        var descriptor = FileChooserDescriptor(true, false, false, true, false, true)
        var chooseFile = FileChooser.chooseFiles(descriptor, e.project, null)
        for (file in chooseFile) {
            println("name = ${file.name}")
            println("path = ${file.path}")
        }

    }
}
