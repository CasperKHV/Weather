package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import com.example.coloreffect.NoteDataReader
import androidx.recyclerview.widget.RecyclerView
import com.example.coloreffect.R
import com.example.coloreffect.CityNote
import com.example.coloreffect.ModelForGSONWeatherClass
import androidx.appcompat.app.AppCompatActivity
import com.example.coloreffect.CitiesListFragment.CitiesListListener
import androidx.navigation.ui.AppBarConfiguration
import com.example.coloreffect.NoteDataSource
import android.os.Bundle
import com.example.coloreffect.MainActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.coloreffect.DataForBundle
import com.example.coloreffect.WeatherResultFragment
import android.content.Intent
import com.example.coloreffect.WeatherResult
import com.example.coloreffect.CitiesListFragment
import android.content.SharedPreferences
import android.content.DialogInterface
import androidx.core.view.GravityCompat
import com.example.coloreffect.FragmentForNV
import android.os.Environment
import com.bumptech.glide.Glide
import android.database.sqlite.SQLiteOpenHelper
import com.example.coloreffect.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
import android.content.ContentValues
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View.OnLongClickListener
import com.example.coloreffect.WeatherSpec
import retrofit2.http.GET
import com.example.coloreffect.NoteDataSourceForHistory
import com.example.coloreffect.NoteDataReaderForHistory
import com.example.coloreffect.WeatherResultFragment.HistoryListListener
import android.content.res.Resources.NotFoundException
import android.app.Activity
import com.example.coloreffect.CheckBoxWeatherResultFragment
import android.content.pm.PackageManager
import android.util.Log
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.coloreffect.HistoryNote
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.lang.StringBuilder

class FragmentForNV : Fragment() {
    private val messageForFile by bindView<EditText>(R.id.edit_text_for_NV)
    var save: Button? = null
    var load: Button? = null
    var delete: Button? = null
    var showImageFromInternet: Button? = null
    var imageFromInternet: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_for_n_v, container, false)
        initializeViews(rootView)
        return rootView
    }

    private fun saveFile() {
        object : Thread() {
            override fun run() {
                if (!isExternalStorageWritable) {
                    Log.d("File", getString(R.string.toast_external_storage_not_found))
                    showLog(getText(R.string.toast_external_storage_not_found))
                    return
                }
                //                File dir = new File(getActivity().getExternalFilesDir(null).getPath());
//                if (!dir.exists()) {// так как мы обращаемся к корню (null вверху), то это проверка не имеет смысла (если нет корня, то и приложения не существует).
//                    // В случае указания конкретного пути вместо null(можно было заменить null на некую вложенность типа "/123/321", т.е. указываем конкретную папку), такая проверка нужна!
//                    dir.mkdirs();
//                }
//
//                File file = new File(dir, NAME_OF_FILE);

//                File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), NAME_OF_FILE);
                val file = File(activity!!.filesDir, NAME_OF_FILE)
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
//                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), NAME_OF_FILE);
                    val file = File(activity!!.filesDir, NAME_OF_FILE)
                    if (!file.exists()) {
                        showLog(getString(R.string.toast_file_not_exist))
                        return
                    }
                    val text = StringBuilder()
                    val br = BufferedReader(FileReader(file))
                    var line: String?

                    // есть текст в br считываем его построчно
                    while (br.readLine().also { line = it } != null) {
                        // добавляем считанное в StringBuilder
                        text.append(line)
                        // перенос на новую строку не считывается поэтому добавляем его
                        text.append('\n')
                    }
                    // последний добавленный перенос лишний, убираем его
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
//                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), NAME_OF_FILE);
                    val file = File(activity!!.filesDir, NAME_OF_FILE)
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

    private fun initializeViews(view: View) {
        save = view.findViewById(R.id.buttonSavePrivate)
        save!!.setOnClickListener(View.OnClickListener { saveFile() })
        load = view.findViewById(R.id.buttonLoadPrivate)
        load!!.setOnClickListener(View.OnClickListener { loadFile() })
        delete = view.findViewById(R.id.buttonDeletePrivate)
        delete!!.setOnClickListener(View.OnClickListener { deleteFile() })
        imageFromInternet = view.findViewById(R.id.imageFromInternet)
        showImageFromInternet = view.findViewById(R.id.showImageFromInternet)
        showImageFromInternet?.setOnClickListener(::showImageFromInternet)
    }

    // проверим доступен ли external storage для записи/чтения
    // обычно он может быть не доступен либо из-за сбоя ОС, либо из-за нехватки оперативной памяти
    private val isExternalStorageWritable: Boolean
        private get() {
            val state = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == state) {
                true
            } else false
        }

    // проверим доступен ли external storage для чтения
    private val isExternalStorageReadable: Boolean
        private get() {
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
            .into(imageFromInternet!!)
    }

    companion object {
        private const val NAME_OF_FILE = "NameOfFile.txt"
    }
}