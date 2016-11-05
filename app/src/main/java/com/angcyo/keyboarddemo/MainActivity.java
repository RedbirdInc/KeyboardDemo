package com.angcyo.keyboarddemo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test";
    View mContentView;

    View mActionBar;
    View mEmojiLayout;
    EditText mEditText;

    RecyclerView mRecyclerView;

    int keyboardHeight = -1;
    int contentHeight = -1;

    boolean isShowKeyboard = false;

    SoftInputLayout mSoftInputLayout;

    ArrayList<String> mArrayList = new ArrayList<>();

    int count = 0;

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

    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        return simpleDateFormat.format(new Date());
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
        mSoftInputLayout = (SoftInputLayout) findViewById(R.id.softinput_layout);
        mSoftInputLayout.setOnSoftInputChangeListener(new SoftInputLayout.OnSoftInputChangeListener() {
            @Override
            public void onSoftInputChange(boolean show, int layoutHeight, int contentHeight) {
                Log.i(TAG, "onSoftInputChange: 键盘是否显示:" + show + " 布局高度:" + layoutHeight + " 内容高度:" + contentHeight);
                if (show) {
                    mRecyclerView.smoothScrollToPosition(mArrayList.size());
                }
            }
        });

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSoftInputLayout.requestShowSoftInput();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new MAdapter());

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

        fab.setVisibility(View.GONE);
    }

    public void onButton(View view) {
    }

    public void onOpen(View view) {
        mSoftInputLayout.showEmojiLayout();
    }

    public void onClose(View view) {
        mSoftInputLayout.hideEmojiLayout();
    }

    public void onSend(View view) {
        String string = mEditText.getText().toString();
        mArrayList.add(TextUtils.isEmpty(string) ? "测试" + count++ : string);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mArrayList.size());
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

    @Override
    public void onBackPressed() {
        if (!mSoftInputLayout.handleBack()) {
            super.onBackPressed();
        }
    }

    public void onDialog(View view) {
        new DemoDialog().show(getSupportFragmentManager(), "dialog");
    }

    public static class MViewHolder extends RecyclerView.ViewHolder {

        public MViewHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * 对话框中弹出表情
     */
    public static class DemoDialog extends AppCompatDialogFragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.dialog_main, (ViewGroup) getDialog().getWindow().findViewById(Window.ID_ANDROID_CONTENT), false);
            final SoftInputLayout softLayout = (SoftInputLayout) view.findViewById(R.id.softinput_layout);
            view.findViewById(R.id.open_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    softLayout.showEmojiLayout();
                }
            });
            view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    softLayout.hideEmojiLayout();
                }
            });
            view.findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DemoDialog().show(getChildFragmentManager(), "dialog");
                }
            });

            initDialog();
            return view;
        }

        private void initDialog() {
            final Dialog dialog = getDialog();
            final Window window = dialog.getWindow();
            final WindowManager.LayoutParams attributes = window.getAttributes();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            attributes.width = -1;
            attributes.height = -1;
            attributes.gravity = Gravity.BOTTOM;
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setAttributes(attributes);
        }
    }

    public class MAdapter extends RecyclerView.Adapter<MViewHolder> {

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_msg_layout, parent, false);
            return new MViewHolder(item);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(0)).setText(getTime());
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(1)).setText(mArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }
    }
}
