package com.example.mypegasus.usingsqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final int FLAG = 1200;
    private SimpleCursorAdapter adapter;
    private EditText etName, etSex;
    private Button btnAdd;

    private Db db;
    private SQLiteDatabase dbRead, dbWrite;

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString();
            String sex = etSex.getText().toString();

            if("".equals(name) || "".equals(sex)) return;
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("sex", sex);
            dbWrite.insert("user", null, cv);

            refreshListView();
        }
    };
    private AdapterView.OnItemLongClickListener listViewItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final int _position = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提醒").setMessage("您确定要删除该项吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Cursor c = adapter.getCursor();
                    c.moveToPosition(_position);

                    int itemID = c.getInt(c.getColumnIndex("_id"));
                    dbWrite.delete("user", "_id=?", new String[]{"" + itemID});
                    refreshListView();
                }
            }).setNegativeButton("取消", null).show();
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etSex = (EditText) findViewById(R.id.etSex);

        findViewById(R.id.btn_add).setOnClickListener(btnClickListener);

        db = new Db(this);
        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();

        adapter = new SimpleCursorAdapter(this, R.layout.user_list_cell, null, new String[] {"name", "sex"}, new int[] {R.id.tvName, R.id.tvSex}, FLAG);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        refreshListView();

        listView.setOnItemLongClickListener(listViewItemLongClickListener);

        /*SQLiteDatabase dbWrite = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", "小张");
        cv.put("sex", "男");
        dbWrite.insert("user", null, cv);
        cv = new ContentValues();
        cv.put("name", "小李");
        cv.put("sex", "女");
        dbWrite.insert("user", null, cv);
        dbWrite.close();*/

        /*SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor c = dbRead.query("user", null, "name like ?", new String[]{"%小%"}, null, null, null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String sex = c.getString(c.getColumnIndex("sex"));
            System.out.println(String.format("name=%s,sex=%s", name, sex));
        }
        dbRead.close();*/
    }

    private void refreshListView() {
        Cursor c = dbRead.query("user", null, null, null, null, null, null);
        adapter.changeCursor(c);
    }
}
