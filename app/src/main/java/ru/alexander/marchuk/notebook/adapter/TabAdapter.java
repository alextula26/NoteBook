package ru.alexander.marchuk.notebook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.alexander.marchuk.notebook.fragment.CurrentNoteFragment;
import ru.alexander.marchuk.notebook.fragment.DoneNoteFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private int mNumberOftabs;

    public static final int CURRENT_NOTE_FRAGMENT_POSITION = 0;
    public static final int DONE_NOTE_FRAGMENT_POSITION = 1;

    private CurrentNoteFragment mCurrentNoteFragment;
    private DoneNoteFragment mDoneNoteFragment;

    public TabAdapter(FragmentManager fm, int numberOftabs) {
        super(fm);
        mNumberOftabs = numberOftabs;
        mCurrentNoteFragment = new CurrentNoteFragment();
        mDoneNoteFragment = new DoneNoteFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case CURRENT_NOTE_FRAGMENT_POSITION:
                return mCurrentNoteFragment;
            case DONE_NOTE_FRAGMENT_POSITION:
                return mDoneNoteFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumberOftabs;
    }
}
