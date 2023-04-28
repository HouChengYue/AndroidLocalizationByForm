package com.hcy.androidlocalizationbyform.translation.dialog

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import javax.swing.*

/**
 * 主体窗口
 */
class ContentDialog(xmlFilePath: String="") : JDialog() {
    private lateinit var root: JPanel
    private lateinit var title: JLabel
    private lateinit var xmlPath: JTextField
    private lateinit var chooseXml: JButton
    private lateinit var xlxFilePath: JTextField
    private lateinit var chooseXlxs: JButton
    private lateinit var summit: JButton
    private lateinit var log: JTextArea


    init {
        xmlPath.text=xmlFilePath
        setContentPane(root)
        isModal=true
        getRootPane().defaultButton=chooseXml
        chooseXml.addActionListener { e->

//            val descriptor = FileChooserDescriptor(true, false, false, true, false, true)
//            "请选择翻译表格文件！".also { descriptor.description = it }
//            val chooseFile = FileChooser.chooseFiles(descriptor, e.project, null)
//            for (file in chooseFile) {
//                println("name = ${file.name}")
//                println("path = ${file.path}")
//            }
        }






    }


}
