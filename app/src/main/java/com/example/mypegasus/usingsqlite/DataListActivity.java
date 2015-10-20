package com.example.mypegasus.usingsqlite;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText etName, etSex;
    private Db db;
    private SQLiteDatabase dbWrite, dbRead;

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
            adapter.notifyItemInserted(adapter.getItemCount());
            listData.add(cv);
            //adapter.notifyItemRangeChanged(2, adapter.getItemCount());

        }
    };
    private List<ContentValues> listData;
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        etName = (EditText) findViewById(R.id.etName);
        etSex = (EditText) findViewById(R.id.etSex);

        findViewById(R.id.btn_add).setOnClickListener(btnClickListener);

        db = new Db(this);
        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();
        Cursor c = dbRead.query("user", null, null, null, null, null, null);
        listData = new ArrayList<>();
        while(c.moveToNext()) {
            ContentValues cv = new ContentValues();
            cv.put("_id", c.getInt(c.getColumnIndex("_id")));
            cv.put("name", c.getString(c.getColumnIndex("name")));
            cv.put("sex", c.getString(c.getColumnIndex("sex")));
            listData.add(cv);
        }

        System.out.println("listData length:" + listData.size());

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        adapter = new MyRecyclerViewAdapter(listData);
        adapter.setItemLongClickListener(new MyRecyclerViewAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DataListActivity.this);
                builder.setTitle("提醒").setMessage("您确定要删除该项吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(DataListActivity.this, "position:" + position + ", which:" + which, Toast.LENGTH_SHORT).show();
                        ContentValues deleteData = listData.remove(position);
                        dbWrite.delete("user", "_id=?", new String[]{"" + deleteData.getAsInteger("_id")});
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(DataListActivity.this, "positon:" + position, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL));
    }

}
