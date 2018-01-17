package com.example.launcherbadgemanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.launcherbadgemanager.badge.LauncherBadgeManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener{
    private EditText mEditText;
    private Button mButton;
    private Button mClearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.badge_input);
        mEditText.setOnClickListener(this);
        mButton = findViewById(R.id.update_badge);
        mButton.setOnClickListener(this);
        mClearButton = findViewById(R.id.clear_badge);
        mClearButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.badge_input:
                break;
            case R.id.update_badge:
                String badgeCount = mEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(badgeCount) && isNumeric(badgeCount)) {
                    LauncherBadgeManager.getInstance().applyUpdatelauncherBadge(getApplicationContext(), Integer.valueOf(badgeCount));
                    finish();
                }
                break;
            case R.id.clear_badge:
                LauncherBadgeManager.getInstance().applyUpdatelauncherBadge(getApplicationContext(), Integer.valueOf("0"));
                finish();
                break;
        }
    }

    /**
     *  判断输入的是否为 数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
