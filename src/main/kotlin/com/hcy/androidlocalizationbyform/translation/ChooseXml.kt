package com.hcy.androidlocalizationbyform.translation

import com.hcy.androidlocalizationbyform.translation.dialog.ContentDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * 从xml 开始弹窗
 */
class ChooseXml : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val path = requireNotNull(data).path
        if (path.contains("strings") && path.endsWith(".xml")) {
            ContentDialog(path).show();
        }
        print(data)
    }

    override fun update(e: AnActionEvent) {
        super.update(e)

        val data = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val path = requireNotNull(data).path
        e.presentation.isEnabled = path.contains("strings") && path.endsWith(".xml")

    }

}
