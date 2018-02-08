package com.emsilabs.coc.cockeyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private MediaPlayer mp;
    private KeyboardView kv;
    private LatinKeyboard keyboard, cockeyboard, currentkeyboard;
    private boolean caps = false;

    @Override public void onCreate() {
        super.onCreate();

        mp = MediaPlayer.create(this, R.raw.four);
        mp.setVolume(10, 10);
    }

    @Override public void onInitializeInterface() {

        keyboard = new LatinKeyboard(this, R.xml.qwerty);
        cockeyboard = new LatinKeyboard(this, R.xml.coc);
    }

    @Override
    public View onCreateInputView() {

            kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        return kv;

    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting)
    {
        super.onStartInput(attribute, restarting);

//        Get package name of current apps
        currentkeyboard = keyboard;
        ActivityManager app = (ActivityManager) this
                .getSystemService(Activity.ACTIVITY_SERVICE);
        String packageName = app.getRunningTasks(1).get(0).topActivity
                .getPackageName();

        currentkeyboard.setImeOptions(getResources(), attribute, packageName);
    }

    @Override public void onFinishInput() {
        super.onFinishInput();


        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        currentkeyboard = keyboard;
        if (kv != null) {
            kv.closing();
        }
    }

    @Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        kv.setKeyboard(currentkeyboard);
        kv.closing();
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                final int options = this.getCurrentInputEditorInfo().imeOptions;
                final int actionId = options & EditorInfo.IME_MASK_ACTION;

                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        sendDefaultEditorAction(true);
//                        Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        sendDefaultEditorAction(true);
//                        Toast.makeText(this, "GO", Toast.LENGTH_LONG).show();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        sendDefaultEditorAction(true);
//                        Toast.makeText(this, "SEND", Toast.LENGTH_LONG).show();
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        sendDefaultEditorAction(true);
//                        Toast.makeText(this, "DONE", Toast.LENGTH_LONG).show();
                        break;

                    default:
//                        Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }


                break;
            case 696969:
                keyboard = new LatinKeyboard(this, R.xml.coc);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
//                currentkeyboard = cockeyboard;
//                kv.setKeyboard(currentkeyboard);
//                kv.closing();
                break;
//            Back to Default Keyboard
            case 6969690:
                keyboard = new LatinKeyboard(this, R.xml.qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
//                  currentkeyboard = keyboard;
//                kv.setKeyboard(currentkeyboard);
//                kv.closing();
                break;

//            CONFIRM
            case 6969691:

                ic.commitText("CONFIRM", 1);
                sendDefaultEditorAction(true);
                break;

//            PICK INPUT METHOD
            case 6969693:

                InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                im.showInputMethodPicker();


                break;

            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }



    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float vol = 1; //This will be half of the default system sound

        switch(keyCode){
            case -4:
                mp.start();
            default:
                am.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            Toast.makeText(this, "Enter", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
