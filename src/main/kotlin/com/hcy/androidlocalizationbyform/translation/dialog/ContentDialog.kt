package com.hcy.androidlocalizationbyform.translation.dialog

import com.fasterxml.aalto.util.TextUtil
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.Objects
import javax.swing.*

/**
 * 主体窗口
 */
class ContentDialog(xmlFilePath: String = "") : JDialog() {
    private lateinit var root: JPanel
    private lateinit var title: JLabel
    private lateinit var xmlPath: JTextField
    private lateinit var btnChooseXml: JButton
    private lateinit var xlxFilePath: JTextField
    private lateinit var btnChooseXlxs: JButton
    private lateinit var btnClose: JButton
    private lateinit var btnSummit: JButton
    private lateinit var log: JTextArea


    init {
        xmlPath.text = xmlFilePath
        setContentPane(root)
        isModal = true
        if (xmlFilePath.isEmpty()) {
            getRootPane().defaultButton = btnChooseXml
        } else {
            getRootPane().defaultButton = btnChooseXlxs
        }
        btnChooseXml.addActionListener { e ->
            val descriptor = FileChooserDescriptor(true, false, false, true, false, true)
            "请选择strings 下的Xml文件！".also { descriptor.description = it }
            val file = FileChooser.chooseFile(descriptor, null, null)
            xmlPath.text= requireNotNull(file).path
        }

        btnChooseXlxs.addActionListener {
            val descriptor = FileChooserDescriptor(true, false, false, true, false, true)
            "请选择表格文件！（xlx、xlxs）".also { descriptor.description = it }
            val file = FileChooser.chooseFile(descriptor, null, null)
            xlxFilePath.text= requireNotNull(file).path
        }

        btnSummit.addActionListener {

        }


        btnClose.addActionListener {
            dispose()
        }

        defaultCloseOperation = DO_NOTHING_ON_CLOSE

        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                super.windowClosed(e)
                onCancel()
            }
        })

        root.registerKeyboardAction(
            { onCancel() },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        )


    }

    private fun onCancel() {
        dispose()
    }


}
