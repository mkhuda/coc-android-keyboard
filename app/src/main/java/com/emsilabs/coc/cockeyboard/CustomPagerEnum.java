package com.emsilabs.coc.cockeyboard;

/**
 * Created by emsi on 08/02/16.
 */
public enum CustomPagerEnum {

    ONE(R.layout.activity_one),
    TWO(R.layout.activity_two),
    THREE(R.layout.activity_three),
    FOUR(R.layout.activity_four),
    FIVE(R.layout.activity_five),
    SIX(R.layout.activity_six),
    SEVEN(R.layout.activity_seven),
    EIGHT(R.layout.activity_eight);

    private int mLayoutResId;

    CustomPagerEnum(int layoutResId) {
        mLayoutResId = layoutResId;
    }


    public int getLayoutResId() {
        return mLayoutResId;
    }

}
