package com.example.lewis.list_window;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    private ListPopupWindow mListWindowUtils;
    private Toolbar mToolbar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(this, initData());
        recyclerView.setAdapter(myAdapter);
        mListWindowUtils = ListPopupWindow.get(MainActivity.this);

        myAdapter.setOnImageViewClickListener(new MyAdapter.OnImageViewClickListener() {
            @Override
            public void onImageViewClick(View view) {
                mListWindowUtils.show(view);
            }
        });

        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        mListWindowUtils.setOnItemClickListener(new ListPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int position, WindowAdapter adapter) {
                Toast.makeText(MainActivity.this, "This is " + adapter.getData(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("input key to search");

        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.parseColor("#66FFFFFF"));
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        return super.onCreateOptionsMenu(menu);
    }

    private List<String> initData() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            result.add(i + "");
        }
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mListWindowUtils != null && mListWindowUtils.isShowing()) {
            mListWindowUtils.dismissWithAnim();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
