package ru.alexander.marchuk.notebook.model;

import ru.alexander.marchuk.notebook.R;

public class NoteSeparator implements Item {

    public static final int TYPE_OVERDUE = R.string.separator_overdue;
    public static final int TYPE_TODAY = R.string.separator_today;
    public static final int TYPE_TOMORROW = R.string.separator_tomorrow;
    public static final int TYPE_FUTURE = R.string.separator_future;

    private int mType;

    public NoteSeparator(int type){
        mType = type;
    }

    @Override
    public boolean isNote() {
        return false;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
