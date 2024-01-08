package com.limelight.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.limelight.binding.input.KeyboardTranslator;

import java.util.ArrayList;

public class KeyboardButtonsPreference extends DialogPreference
{
    private final Context context;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    public KeyboardButtonsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected View onCreateDialogView() {
        listView = new ListView(context);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice);
        listView.setAdapter(adapter);

        String activeKeys = getPersistedString("");

        for (int i = 0, l = 0; i < 255; i++) {
            short v = KeyboardTranslator.translateKeycode(i);
            if(v == 0) continue;
            adapter.add(KeyEvent.keyCodeToString(i));
            listView.setItemChecked(l++, activeKeys.indexOf(i) != -1);
        }

        return listView;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(!positiveResult) return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < adapter.getCount(); i++) {
            if(!listView.isItemChecked(i)) continue;

            Log.d("KBS", String.format("%d", i));
            String item = adapter.getItem(i);
            if(item == null) continue; //not sure how it will be null, but better be safe than sorry
            int keyCode = KeyEvent.keyCodeFromString(item);
            if(keyCode == KeyEvent.KEYCODE_UNKNOWN) continue;
            sb.append((char)keyCode);
        }

        String encodedString = sb.toString();

        persistString(encodedString);
    }
}
