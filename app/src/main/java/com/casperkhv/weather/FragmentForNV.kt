package com.casperkhv.weather

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

class FragmentForNV : Fragment() {
    private val messageForFile by bindView<EditText>(R.id.edit_text_for_NV)
    private val save by bindView<Button>(R.id.buttonSavePrivate)
    private val load by bindView<Button>(R.id.buttonLoadPrivate)
    private val delete by bindView<Button>(R.id.buttonDeletePrivate)
    private val showImageFromInternet by bindView<Button>(R.id.showImageFromInternet)
    private val imageFromInternet by bindView<ImageView>(R.id.imageFromInternet)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_for_n_v, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()

    }

    private fun saveFile() {
        object : Thread() {
            override fun run() {
                if (!isExternalStorageWritable) {
                    Log.d("File", getString(R.string.toast_external_storage_not_found))
                    showLog(getText(R.string.toast_external_storage_not_found))
                    return
                }
                val file = File(requireActivity().filesDir, NAME_OF_FILE)
                val message = messageForFile.text.toString()
                if (!message.isEmpty()) {
                    try {
                        val outputStream = FileOutputStream(file, false)
                        outputStream.write(message.toByteArray())
                        outputStream.flush()
                        outputStream.close()
                    } catch (e: Exception) {
                        showLog(e.toString())
                    }
                }
            }
        }.start()
    }

    private fun loadFile() {
        object : Thread() {
            override fun run() {
                if (!isExternalStorageReadable) {
                    showLog(getText(R.string.toast_external_storage_not_found))
                    return
                }
                try {
                    val file = File(requireActivity().filesDir, NAME_OF_FILE)
                    if (!file.exists()) {
                        showLog(getString(R.string.toast_file_not_exist))
                        return
                    }
                    val text = StringBuilder()
                    val br = BufferedReader(FileReader(file))
                    var line: String?

                    while (br.readLine().also { line = it } != null) {
                        text.append(line)
                        text.append('\n')
                    }
                    text.deleteCharAt(text.length - 1)
                    br.close()
                    messageForFile.setText(text)
                } catch (e: Exception) {
                    showLog(e.toString())
                }
            }
        }.start()
    }

    private fun deleteFile() {
        object : Thread() {
            override fun run() {
                try {
                    val file = File(requireActivity().filesDir, NAME_OF_FILE)
                    if (file.exists()) {
                        if (file.delete()) {
                            showLog(getString(R.string.toast_file_deleted))
                        }
                    } else {
                        showLog(getString(R.string.toast_file_not_exist))
                    }
                    messageForFile.setText("")
                } catch (e: Exception) {
                    showLog(e.toString())
                }
            }
        }.start()
    }

    private fun setOnClickListeners() {
        save.setOnClickListener { saveFile() }
        load.setOnClickListener { loadFile() }
        delete.setOnClickListener { deleteFile() }
        showImageFromInternet.setOnClickListener(::showImageFromInternet)
    }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == state) {
                true
            } else false
        }

    private val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
                true
            } else false
        }

    private fun showLog(toastMessage: CharSequence) {
        Log.d("File", toastMessage.toString())
    }

    private fun showImageFromInternet(view: View) {
        Glide
            .with(view)
            .load("https://img-fotki.yandex.ru/get/4130/36014149.180/0_80731_c5cd79f0_M.png")
            .override(500)
            .into(imageFromInternet)
    }

    companion object {
        private const val NAME_OF_FILE = "NameOfFile.txt"
    }
}