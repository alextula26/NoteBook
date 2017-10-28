package ru.alexander.marchuk.notebook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import ru.alexander.marchuk.notebook.adapter.TabAdapter;
import ru.alexander.marchuk.notebook.dialog.AddingNoteDialogFragment;
import ru.alexander.marchuk.notebook.dialog.EditNoteDialogFragment;
import ru.alexander.marchuk.notebook.fragment.CurrentNoteFragment;
import ru.alexander.marchuk.notebook.fragment.NoteFragment;
import ru.alexander.marchuk.notebook.fragment.DoneNoteFragment;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;

public class MainActivity extends AppCompatActivity implements
        AddingNoteDialogFragment.AddingNoteListener,
        CurrentNoteFragment.AddingNoteInDoneListener,
        DoneNoteFragment.NoteRestoreListener,
        EditNoteDialogFragment.EditingNoteListener{

    TabAdapter mTabAdapter;
    SearchView mSearchView;

    private FragmentManager fragmentManager;
    private NoteFragment mCurrentNoteFragment;
    private NoteFragment mDoneNoteFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
    }

    private void setUI() {

        fragmentManager = getSupportFragmentManager();

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);
        collapsingToolbar.setTitle(getResources().getText(R.string.app_name));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            setSupportActionBar(toolbar);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_exercises));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_programm));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        mTabAdapter = new TabAdapter(fragmentManager, 2);
        viewPager.setAdapter(mTabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mCurrentNoteFragment = (CurrentNoteFragment) mTabAdapter.getItem(TabAdapter.CURRENT_NOTE_FRAGMENT_POSITION);
        mDoneNoteFragment = (DoneNoteFragment) mTabAdapter.getItem(TabAdapter.DONE_NOTE_FRAGMENT_POSITION);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCurrentNoteFragment.findNote(newText);
                mDoneNoteFragment.findNote(newText);
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingNoteDialogFragment = new AddingNoteDialogFragment();
                addingNoteDialogFragment.show(fragmentManager, "AddingNoteDialogFragment");
            }
        });

    }

    @Override
    public void onResume() { super.onResume();}

    @Override
    public void onNoteAdded(NoteModel newNote) {
        mCurrentNoteFragment.addNote(newNote, true);
    }

    @Override
    public void onNoteAddingCancel() {
        Toast.makeText(this, "Exercises adding Cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteAddedDone(NoteModel note) {
        mDoneNoteFragment.addNote(note, false);
    }

    @Override
    public void onNoteRestore(NoteModel note) {
        mCurrentNoteFragment.addNote(note, false);
    }

    @Override
    public void onNoteEdited(NoteModel updateNote) {
        mCurrentNoteFragment.updateNote(updateNote);
        NoteModelLab.get(getApplicationContext()).updateNote(updateNote);
    }
}