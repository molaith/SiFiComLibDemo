package com.sificomlib.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sificomlib.R;

public class FragementAttachActivity extends AppCompatActivity {
    private static Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_attach_fragemnet);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_fragment_up_in, R.anim.anim_fragment_down_out).add(R.id.fragment_container, fragment).commit();
            return;
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment);
            fragment = null;
        }
        super.onDestroy();
    }

    public static void attachLaunch(Context context,Fragment attachFragment) {
        fragment = attachFragment;
        Intent intent = new Intent(context, FragementAttachActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
