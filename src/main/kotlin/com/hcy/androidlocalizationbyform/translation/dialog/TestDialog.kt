package com.hcy.androidlocalizationbyform.translation.dialog

import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import kotlin.system.exitProcess

class TestDialog : JDialog() {
    private lateinit var  contentPane: JPanel
    private lateinit var  buttonOK: JButton
    private lateinit var  buttonCancel: JButton

    init {
        setContentPane(contentPane)
        isModal = true
        getRootPane().defaultButton = buttonOK
        buttonOK.addActionListener { onOK() }
        buttonCancel.addActionListener { onCancel() }

        // call onCancel() when cross is clicked
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                onCancel()
            }
        })

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
            { onCancel() },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        )
    }

    private fun onOK() {
        // add your code here
        dispose()
    }

    private fun onCancel() {
        // add your code here if necessary
        dispose()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val dialog = TestDialog()
            dialog.pack()
            dialog.isVisible = true
            exitProcess(0)



        }
    }
}
