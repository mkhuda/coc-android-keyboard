package com.emsilabs.coc.cockeyboard;


import android.inputmethodservice.Keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import android.view.inputmethod.EditorInfo;

public class LatinKeyboard extends Keyboard {

    private Key mEnterKey;

    public LatinKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y,
                                   XmlResourceParser parser) {
        Key key = new LatinKey(res, parent, x, y, parser);
        if (key.codes[0] == -4) {
            mEnterKey = key;
        }
        return key;
    }

    /**
     * This looks at the ime options given by the current editor, to set the
     * appropriate label on the keyboard's enter key (if it has one).
     */
    void setImeOptions(Resources res, EditorInfo ei, String activity) {

        if (mEnterKey == null) {
            return;
        }

        switch (ei.imeOptions & (EditorInfo.IME_MASK_ACTION|EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
            case EditorInfo.IME_ACTION_GO:
                mEnterKey.iconPreview = null;
                mEnterKey.icon = null;
                mEnterKey.label = res.getText(R.string.label_go_key);
                break;
            case EditorInfo.IME_ACTION_NEXT:
                mEnterKey.iconPreview = null;
                mEnterKey.icon = null;
                mEnterKey.label = res.getText(R.string.label_next_key);
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                mEnterKey.icon = res.getDrawable(
                        android.R.drawable.ic_menu_search);
                mEnterKey.label = null;
                break;
            case EditorInfo.IME_ACTION_SEND:
                mEnterKey.iconPreview = null;
                mEnterKey.icon = null;
                mEnterKey.label = res.getText(R.string.label_send_key);
                break;
            case EditorInfo.IME_ACTION_DONE:
                mEnterKey.iconPreview = null;
                if (activity.equals("com.supercell.clashofclans")) {

                    mEnterKey.icon = res.getDrawable(android.R.drawable.ic_menu_set_as);
                    mEnterKey.label = null;
                } else {

                    mEnterKey.icon = null;
                    mEnterKey.label = "DONE";

                }
                break;
            default:
                mEnterKey.icon = res.getDrawable(
                        R.drawable.sym_keyboard_return);
                mEnterKey.label = null;
                break;
        }
    }

    static class LatinKey extends Keyboard.Key {

        public LatinKey(Resources res, Keyboard.Row parent, int x, int y, XmlResourceParser parser) {
            super(res, parent, x, y, parser);
        }

        /**
         * Overriding this method so that we can reduce the target area for the key that
         * closes the keyboard.
         */
        @Override
        public boolean isInside(int x, int y) {
            return super.isInside(x, codes[0] == KEYCODE_CANCEL ? y - 10 : y);
        }
    }

}
