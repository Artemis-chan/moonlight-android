package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.limelight.Game;
import com.limelight.binding.input.KeyboardTranslator;

public class KeyboardButton extends DigitalButton {

    private static final String TAG = "KB_BTN";

    public KeyboardButton(final VirtualController controller, final int layer, final Context context, short keycode) {
        super(controller, keycode, layer, context);

        String name = KeyboardTranslator.getLabel(keycode);
        setText(name);

        setIcon(-1);

        final Game game = (Game)context;
        if(game == null)
        {
            return;
        }

        addDigitalButtonListener(new DigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                game.keyboardEvent(true, keycode);
                Log.d(TAG, "onClick: " + name);
            }

            @Override
            public void onLongClick() {
                Log.d(TAG, "onLongClick: " + name);
            }

            @Override
            public void onRelease() {
                game.keyboardEvent(false, keycode);
                Log.d(TAG, "onRelease: " + name);
            }
        });
    }

    @Override
    public String getPrefKey() {
        return "KB_" + elementId;
    }
}
