package com.angcyo.keyboarddemo;

import android.app.Activity;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test";
    View mContentView;

    View mActionBar;
    View mEmojiLayout;
    EditText mEditText;

    int keyboardHeight = -1;
    int contentHeight = -1;

    boolean isShowKeyboard = false;

    /**
     * 屏幕高度, 包括状态栏的高度,和底部虚拟导航栏的高度
     */
    public static int getScreenHeight(Activity activity) {
        Rect out = new Rect();
        activity.getWindow().getDecorView().getHitRect(out);
        return out.height();
    }

    /**
     * 窗口的高度,包括状态栏的高度
     */
    public static int getWindowHeight(Activity activity) {
        Rect out = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHitRect(out);
        return out.height();
    }

    /**
     * 导航栏的高度
     */
    public static int getNavigationHeight(Activity activity) {
        int screenHeight = getScreenHeight(activity);
        int windowHeight = getWindowHeight(activity);
        return screenHeight - windowHeight;
    }

    /**
     * 状态栏的高度
     */
    public static int getStatusHeight(Activity activity) {
        Rect out = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(out);
        return out.top;
    }

    public static void hideSoftInput(Activity activity, EditText editText) {
        ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mContentView = findViewById(R.id.content_main);
        mActionBar = findViewById(R.id.action_bar);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mEmojiLayout = findViewById(R.id.emoji_layout);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                View decorView = getWindow().getDecorView();
                View windowView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                logScreen();

                logView(mActionBar);

                logView(mContentView);
                logView(decorView);

                logHit(mContentView);
                logHit(decorView);
                logHit(windowView);

                logVisible(mContentView);
                logVisible(decorView);//test test
                logVisible(windowView);//test test

                logHeight();
            }
        });

        mContentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                contentHeight = bottom - top;
                int height = getWindowHeight(MainActivity.this) - bottom;
                if (height > 200) {
                    isShowKeyboard = true;
                    keyboardHeight = height;
                } else {
                    isShowKeyboard = false;
                }
            }
        });

    }

    public void hideEmoji() {
        LinearLayout.LayoutParams mEmojiLayoutLayoutParams = (LinearLayout.LayoutParams) mEmojiLayout.getLayoutParams();
        mEmojiLayoutLayoutParams.weight = 0;
        mEmojiLayout.setVisibility(View.GONE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        layoutParams.height = contentHeight;
        layoutParams.weight = 1;

        mContentView.requestLayout();
    }

    public void onButton(View view) {
        if (isShowKeyboard) {
            hideSoftInput(this, mEditText);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
            layoutParams.height = contentHeight;
            layoutParams.weight = 0;

            LinearLayout.LayoutParams mEmojiLayoutLayoutParams = (LinearLayout.LayoutParams) mEmojiLayout.getLayoutParams();
            mEmojiLayoutLayoutParams.weight = 1;
            mEmojiLayout.setVisibility(View.VISIBLE);

            isShowKeyboard = false;
        } else {
            hideEmoji();
        }
//        mEmojiLayoutLayoutParams.height = keyboardHeight;
//        mEmojiLayout.setLayoutParams(mEmojiLayoutLayoutParams);


//        layoutParams.height = getWindowHeight(this) - mContentView.getTop();
//        final int keyboardHeight = this.keyboardHeight;
//        delay(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.LayoutParams mEmojiLayoutLayoutParams = mEmojiLayout.getLayoutParams();
//                mEmojiLayoutLayoutParams.height = keyboardHeight;
//                mEmojiLayout.setLayoutParams(mEmojiLayoutLayoutParams);
//            }
//        });
//        hideSoftInput(this, mContentView);
    }

    private void delay(Runnable runnable) {
        mContentView.postDelayed(runnable, 300);
    }

    private void logHeight() {
        String log = String.format("屏幕高度:%s 窗口高度:%s 状态栏高度:%s 导航栏高度:%s",
                getScreenHeight(this), getWindowHeight(this), getStatusHeight(this), getNavigationHeight(this));
        Log.i(TAG, "logHeight: " + log);
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
