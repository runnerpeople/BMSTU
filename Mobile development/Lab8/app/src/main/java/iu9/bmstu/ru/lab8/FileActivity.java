package iu9.bmstu.ru.lab8;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileActivity extends ListActivity {

    private static final String TAG = FileActivity.class.getName();

    private static Boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static Boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    // https://stackoverflow.com/a/40582634
    private static final String[] KNOWN_PHYSICAL_PATHS = new String[]{
            "/storage/sdcard0",
            "/storage/sdcard1",                 //Motorola Xoom
            "/storage/extsdcard",               //Samsung SGS3
            "/storage/sdcard0/external_sdcard", //User request
            "/mnt/extsdcard",
            "/mnt/sdcard/external_sd",          //Samsung galaxy family
            "/mnt/sdcard/ext_sd",
            "/mnt/external_sd",
            "/mnt/media_rw/sdcard1",            //4.4.2 on CyanogenMod S3
            "/removable/microsd",               //Asus transformer prime
            "/mnt/emmc",
            "/storage/external_SD",             //LG
            "/storage/ext_sd",                  //HTC One Max
            "/storage/removable/sdcard1",       //Sony Xperia Z1
            "/data/sdext",
            "/data/sdext2",
            "/data/sdext3",
            "/data/sdext4",
            "/sdcard1",                         //Sony Xperia Z
            "/sdcard2",                         //HTC One M8s
            "/storage/microsd"                  //ASUS ZenFone 2
    };

    private static final String EXTERNAL_STORAGE = System.getenv("EXTERNAL_STORAGE");
    private static final String SECONDARY_STORAGES = System.getenv("SECONDARY_STORAGE");
    private static final String EMULATED_STORAGE_TARGET = System.getenv("EMULATED_STORAGE_TARGET");


    public static String[] getStorageDirectories(Context context) {
        // Final set of paths
        final Set<String> availableDirectoriesSet = new HashSet<>();

        if (!TextUtils.isEmpty(EMULATED_STORAGE_TARGET)) {
            // Device has an emulated storage
            availableDirectoriesSet.add(getEmulatedStorageTarget());
        } else {
            // Device doesn't have an emulated storage
            availableDirectoriesSet.addAll(getExternalStorage(context));
        }

        // Add all secondary storages
        Collections.addAll(availableDirectoriesSet, getAllSecondaryStorages());

        String[] storagesArray = new String[availableDirectoriesSet.size()];
        return availableDirectoriesSet.toArray(storagesArray);
    }

    private static Set<String> getExternalStorage(Context context) {
        final Set<String> availableDirectoriesSet = new HashSet<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Solution of empty raw emulated storage for android version >= marshmallow
            // because the EXTERNAL_STORAGE become something like: "/Storage/A5F9-15F4",
            // so we can't access it directly
            File[] files = context.getExternalFilesDirs(null);
            for (File file : files) {
                if (file != null) {
                    String applicationSpecificAbsolutePath = file.getAbsolutePath();
                    String rootPath = applicationSpecificAbsolutePath.substring(
                            0,
                            applicationSpecificAbsolutePath.indexOf("Android/data")
                    );
                    availableDirectoriesSet.add(rootPath);
                }
            }
        } else {
            if (TextUtils.isEmpty(EXTERNAL_STORAGE)) {
                availableDirectoriesSet.addAll(getAvailablePhysicalPaths());
            } else {
                // Device has physical external storage; use plain paths.
                availableDirectoriesSet.add(EXTERNAL_STORAGE);
            }
        }
        return availableDirectoriesSet;
    }

    private static String getEmulatedStorageTarget() {
        String rawStorageId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // External storage paths should have storageId in the last segment
            // i.e: "/storage/emulated/storageId" where storageId is 0, 1, 2, ...
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            final String[] folders = File.separator.split(path);
            final String lastSegment = folders[folders.length - 1];
            if (!TextUtils.isEmpty(lastSegment) && TextUtils.isDigitsOnly(lastSegment)) {
                rawStorageId = lastSegment;
            }
        }

        if (TextUtils.isEmpty(rawStorageId)) {
            return EMULATED_STORAGE_TARGET;
        } else {
            return EMULATED_STORAGE_TARGET + File.separator + rawStorageId;
        }
    }

    private static String[] getAllSecondaryStorages() {
        if (!TextUtils.isEmpty(SECONDARY_STORAGES)) {
            // All Secondary SD-CARDs split into array
            return SECONDARY_STORAGES.split(File.pathSeparator);
        }
        return new String[0];
    }

    private static List<String> getAvailablePhysicalPaths() {
        List<String> availablePhysicalPaths = new ArrayList<>();
        for (String physicalPath : KNOWN_PHYSICAL_PATHS) {
            File file = new File(physicalPath);
            if (file.exists()) {
                availablePhysicalPaths.add(physicalPath);
            }
        }
        return availablePhysicalPaths;
    }


    private int getCurrentChoice() {
        String currentDirectory = buttonDirectory.getText().toString();
        if (getString(R.string.app).equals(currentDirectory))
            return 0;
        else if (getString(R.string.internal).equals(currentDirectory))
            return 1;
        else if (getString(R.string.sd_card).equals(currentDirectory))
            return 2;
        else
            return -1;
    }

    private void initAdapter() {
        File filesDir = currentDirectory;
        if (filesDir.canRead()) {
            final ArrayList<Drawable> icons = new ArrayList<>();
            final ArrayList<String> files = new ArrayList<>();

            for (File file : filesDir.listFiles()) {
                files.add(file.getName());
                if (file.isFile())
                    icons.add(getDrawable(R.drawable.ic_file_black_24dp));
                else
                    icons.add(getDrawable(R.drawable.ic_folder_black_24dp));
            }


            if (!files.isEmpty()) {
                adapter = new ArrayAdapter<Drawable>(this, R.layout.item_list, icons) {

                    @Override
                    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                        View view = View.inflate(getApplicationContext(), R.layout.item_list, null);

                        ImageView iconView = view.findViewById(R.id.list_item_icon);
                        TextView textView = view.findViewById(R.id.list_item_text);

                        iconView.setImageDrawable(icons.get(position));
                        textView.setText(files.get(position));
                        return view;
                    }

                };
                setListAdapter(adapter);
            }
            else {
                adapter = new ArrayAdapter<>(this, R.layout.item_list);
                setListAdapter(adapter);
            }
        } else {
            Snackbar.make(findViewById(R.id.main_activity), "Запрещено чтение этой директории", Snackbar.LENGTH_LONG).show();
        }
    }


    private void changePath(String path) throws IOException {
        currentDirectory = new File(path);

        if (path.contains(getPackageName())) {
            int index = path.indexOf(getPackageName());
            buttonDirectory.setText(getString(R.string.app));
            textViewDirectory.setText(path.substring(index + getPackageName().length()));
        }
        else if (path.contains(Environment.getExternalStorageDirectory().getCanonicalPath())) {
            buttonDirectory.setText(getString(R.string.internal));
            textViewDirectory.setText(path.substring(Environment.getExternalStorageDirectory().getCanonicalPath().length()));
        }
        else if (path.contains(externalDirectory)) {
            buttonDirectory.setText(getString(R.string.sd_card));
            textViewDirectory.setText(path.substring(externalDirectory.length() ));
        }

        initAdapter();
    }


    private Button buttonDirectory;
    private FloatingActionButton buttonCreate;

    private TextView textViewDirectory;
    private File currentDirectory;

    private String externalDirectory;

    private ArrayAdapter<Drawable> adapter;

    private AlertDialog dlg;
    private AlertDialog b;

    private final static int requestCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        List<String> list = new ArrayList<>(Arrays.asList(getStorageDirectories(getApplicationContext())));
        list.remove(Environment.getExternalStorageDirectory().getPath());
        if (!list.isEmpty())
            externalDirectory = list.get(0).substring(0,list.get(0).length()-1);

        textViewDirectory = findViewById(R.id.text_current_dir);

        buttonDirectory = findViewById(R.id.changeDirectoryButton);
        buttonCreate = findViewById(R.id.createButton);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileActivity.this);
                LayoutInflater inflater = FileActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_create, null);
                dialogBuilder.setView(dialogView);

                final EditText editName = dialogView.findViewById(R.id.editName);
                final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);


                dialogBuilder.setTitle("Создать");
                dialogBuilder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        File newFile = new File(currentDirectory,editName.getText().toString());

                        int selectedID = radioGroup.getCheckedRadioButtonId();
                        final RadioButton radioButton = dialogView.findViewById(selectedID);

                        if (radioButton.getText().equals(getString(R.string.file))) {
                            if (newFile.getParentFile().canWrite()) {
                                try {
                                    newFile.createNewFile();
                                } catch (IOException ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                            } else {
                                Snackbar.make(findViewById(R.id.file_activity), "Нет разрешения для создания файла", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else {
                            if (newFile.getParentFile().canWrite()) {
                                newFile.mkdir();
                            } else {
                                Snackbar.make(findViewById(R.id.file_activity), "Нет разрешения для создания папки", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        initAdapter();
                    }
                });
                dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        b.dismiss();
                    }
                });

                b = dialogBuilder.create();
                b.show();
            }
        });

        buttonDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(FileActivity.this);
                builder.setTitle("Выбрать директорию");

                CharSequence[] values;
                if (externalDirectory != null) {
                    values = new CharSequence[] {
                            getString(R.string.app),
                            getString(R.string.internal),
                            getString(R.string.sd_card)
                    };
                }
                else {
                    values = new CharSequence[] {
                            getString(R.string.app),
                            getString(R.string.internal)
                    };
                }
                builder.setSingleChoiceItems(values, getCurrentChoice(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0: {
                                try {
                                    changePath(getApplicationContext().getFilesDir().getPath());
                                }
                                catch (IOException ex) {
                                    Log.e(TAG,ex.getMessage());
                                }
                                break;
                            }
                            case 1: {
                                try {
                                    changePath(Environment.getExternalStorageDirectory().getPath());
                                }
                                catch (IOException ex) {
                                    Log.e(TAG,ex.getMessage());
                                }
                                break;
                            }
                            case 2: {
                                try {
                                    changePath(externalDirectory);
                                }
                                catch (IOException ex) {
                                    Log.e(TAG,ex.getMessage());
                                }
                                break;
                            }
                        }
                        dlg.dismiss();
                    }
                });

                dlg = builder.create();
                dlg.show();
            }
        });

        try {
            changePath(getApplicationContext().getFilesDir().getPath());
        }
        catch (IOException ex) {
            Log.e(TAG,ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        try {
            File parentDirectory = currentDirectory.getParentFile();
            boolean found = false;
            if (parentDirectory.getAbsolutePath().equals(getApplicationContext().getFilesDir().getParentFile().getParent())) {
                found = true;
            } else if (parentDirectory.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getParent())) {
                found = true;
            } else if (parentDirectory.getAbsolutePath().equals((new File(externalDirectory)).getParent())) {
                found = true;
            }

            if (!found)
                changePath(parentDirectory.getPath());
            else
                finish();

        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (currentDirectory.listFiles()[position].isDirectory()) {
            currentDirectory = currentDirectory.listFiles()[position];
            try {
                changePath(currentDirectory.getCanonicalPath());
            }
            catch (IOException ex) {
                Log.e(TAG,ex.getMessage());
            }
        }

    }

}


