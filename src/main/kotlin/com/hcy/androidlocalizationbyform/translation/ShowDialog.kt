package com.hcy.androidlocalizationbyform.translation

import com.hcy.androidlocalizationbyform.translation.dialog.TestDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ShowDialog : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
//        ContentDialog().show()
        val dialog = TestDialog()
        dialog.pack()
        dialog.isVisible = true
    }
}
