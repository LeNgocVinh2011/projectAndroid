package com.example.app_rtsp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app_rtsp.Adapter.CamListAdapter;
import com.example.app_rtsp.Database.DataHelper;
import com.example.app_rtsp.Models.Camera_Link;
import com.example.app_rtsp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btn_Add_Layout;
    private ListView listData;
    private TextView txtEmpty;
    private DataHelper db;
    private static final int MENU_ITEM_EDIT = 1;
    private static final int MENU_ITEM_DELETE = 2;
    private CamListAdapter listViewAdapter;
    private final List<Camera_Link> camList = new ArrayList<Camera_Link>();
    private Intent intent;

    private SharedPreferences sharedPreferences;
    private float keyTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        References();
        setListViewAdapter();

        sharedPreferences = getSharedPreferences("pref", 0);
        keyTheme = sharedPreferences.getFloat("keyTheme", -1);

        setActivityTheme((int) keyTheme);

        btn_Add_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Camera_Link cam = (Camera_Link) adapterView.getAdapter().getItem(i);
                intent = new Intent(MainActivity.this, VideoView.class);
                intent.putExtra("cam_link", cam.getCaLink());
                startActivity(intent);
            }
        });

        registerForContextMenu(listData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_header, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_ITEM_EDIT , 0, "Edit");
        menu.add(0, MENU_ITEM_DELETE, 1, "Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Camera_Link selectedCam = (Camera_Link) this.listData.getItemAtPosition(info.position);
        if(item.getItemId() == MENU_ITEM_DELETE){
            new AlertDialog.Builder(this)
                    .setTitle("Delete Camera")
                    .setMessage("Are you sure ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteCam(selectedCam);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if(item.getItemId() == MENU_ITEM_EDIT){
            intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("camId", selectedCam.getId());
            startActivity(intent);
        }
        else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setting_app){
            intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        camList.clear();
        setListViewAdapter();
        displayEmptyText();
    }

    public void setActivityTheme(int theme)
    {
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    private void setListViewAdapter(){
        camList.addAll(getAllListViewAdapter());
        listViewAdapter = new CamListAdapter(this, R.layout.row_layout, camList);
        listData.setAdapter(listViewAdapter);
    }

    private List<Camera_Link> getAllListViewAdapter(){
        db = new DataHelper(this);
        List<Camera_Link> list = db.getListCam();
        return list;
    }

    private void deleteCam(Camera_Link cam)  {
        DataHelper db = new DataHelper(this);
        db.deleteCam(cam);
        camList.remove(cam);
        listViewAdapter.notifyDataSetChanged();
        displayEmptyText();
    }

    private void References(){
        btn_Add_Layout = findViewById(R.id.btnToAdd);
        listData = findViewById(R.id.listCamera);
        txtEmpty = findViewById(R.id.textEmpty);
    }

    private void displayEmptyText(){
        if(camList.size() == 0){
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
    }
}