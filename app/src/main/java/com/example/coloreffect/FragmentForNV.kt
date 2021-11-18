package com.example.coloreffect;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;


public class FragmentForNV extends Fragment {

    private static final String NAME_OF_FILE = "NameOfFile.txt";
    EditText messageForFile;
    Button save;
    Button load;
    Button delete;
    Button showImageFromInternet;
    ImageView imageFromInternet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_for_n_v, container, false);
        initializeViews(rootView);

        return rootView;
    }

    private void saveFile() {
        new Thread() {
            @Override
            public void run() {
                if (!isExternalStorageWritable()) {
                    showLog(getText(R.string.toast_external_storage_not_found));
                    return;
                }
//                File dir = new File(getActivity().getExternalFilesDir(null).getPath());
//                if (!dir.exists()) {// так как мы обращаемся к корню (null вверху), то это проверка не имеет смысла (если нет корня, то и приложения не существует).
//                    // В случае указания конкретного пути вместо null(можно было заменить null на некую вложенность типа "/123/321", т.е. указываем конкретную папку), такая проверка нужна!
//                    dir.mkdirs();
//                }
//
//                File file = new File(dir, NAME_OF_FILE);

//                File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), NAME_OF_FILE);
                File file = new File(getActivity().getFilesDir(), NAME_OF_FILE);


                String message = messageForFile.getText().toString();
                if (!message.isEmpty()) {
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file, false);
                        outputStream.write(message.getBytes());
                        outputStream.flush();
                        outputStream.close();

                    } catch (Exception e) {
                        showLog(e.toString());
                    }
                }
            }
        }.start();


    }

    private void loadFile() {
        new Thread() {
            @Override
            public void run() {
                if (!isExternalStorageReadable()) {
                    showLog(getText(R.string.toast_external_storage_not_found));
                    return;
                }
                try {
//                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), NAME_OF_FILE);
                    File file = new File(getActivity().getFilesDir(), NAME_OF_FILE);
                    if (!file.exists()) {
                        showLog(getString(R.string.toast_file_not_exist));
                        return;
                    }
                    StringBuilder text = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    // есть текст в br считываем его построчно
                    while ((line = br.readLine()) != null) {
                        // добавляем считанное в StringBuilder
                        text.append(line);
                        // перенос на новую строку не считывается поэтому добавляем его
                        text.append('\n');
                    }
                    // последний добавленный перенос лишний, убираем его
                    text.deleteCharAt(text.length() - 1);
                    br.close();
                    messageForFile.setText(text);
                } catch (Exception e) {
                    showLog(e.toString());
                }


            }
        }.start();
    }

    private void deleteFile() {
        new Thread() {
            @Override
            public void run() {
                try {
//                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), NAME_OF_FILE);
                    File file = new File(getActivity().getFilesDir(), NAME_OF_FILE);
                    if (file.exists()) {
                        if (file.delete()) {
                            showLog(getString(R.string.toast_file_deleted));
                        }
                    } else {
                        showLog(getString(R.string.toast_file_not_exist));
                    }
                    messageForFile.setText("");
                } catch (Exception e) {
                    showLog(e.toString());
                }
            }
        }.start();
    }

    private void initializeViews(View view) {
        messageForFile = view.findViewById(R.id.edit_text_for_NV);
        save = view.findViewById(R.id.buttonSavePrivate);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });
        load = view.findViewById(R.id.buttonLoadPrivate);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFile();
            }
        });
        delete = view.findViewById(R.id.buttonDeletePrivate);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
            }
        });
        imageFromInternet = view.findViewById(R.id.imageFromInternet);
        showImageFromInternet = view.findViewById(R.id.showImageFromInternet);
        showImageFromInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageFromInternet(v);
            }
        });


    }

    // проверим доступен ли external storage для записи/чтения
    // обычно он может быть не доступен либо из-за сбоя ОС, либо из-за нехватки оперативной памяти
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // проверим доступен ли external storage для чтения
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void showLog(CharSequence toastMessage) {
//        Toast toast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
//        toast.show();
        Log.d("File", toastMessage.toString());
    }

    private void showImageFromInternet(View view) {
        Glide
                .with(view)
                .load("https://img-fotki.yandex.ru/get/4130/36014149.180/0_80731_c5cd79f0_M.png")
                .override(500)
                .into(imageFromInternet);
    }

}