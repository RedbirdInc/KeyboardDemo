package com.angcyo.keyboarddemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test";
    View mContentView;

    View mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mContentView = findViewById(R.id.content_main);
        mActionBar = findViewById(R.id.action_bar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                View decorView = getWindow().getDecorView();
                logScreen();

                logView(mActionBar);

                logView(mContentView);
                logView(decorView);

                logHit(mContentView);
                logHit(decorView);

                logVisible(mContentView);
                logVisible(decorView);//test
            }
        });


    }

    private void logScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.i(TAG, "onScreen: H:" + displayMetrics.heightPixels + "  W:" + displayMetrics.widthPixels + " D:" + displayMetrics.density);
        Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
        Log.i(TAG, "onScreen: H:" + defaultDisplay.getHeight() + "  W:" + defaultDisplay.getWidth() + " ID:" + defaultDisplay.getDisplayId());

    }

    private void logView(View view) {
        Log.i(TAG, "onClick: H:" + view.getMeasuredHeight() + "  W:" + view.getMeasuredWidth()
                + "  T:" + view.getTop() + "  B:" + view.getBottom());
    }

    private void logHit(View view) {
        Rect out = new Rect();
        view.getHitRect(out);
        Log.i(TAG, "logHit: " + out);
    }

    private void logVisible(View view) {
        Rect out = new Rect();
        view.getWindowVisibleDisplayFrame(out);
        Log.i(TAG, "logVisible: " + out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
