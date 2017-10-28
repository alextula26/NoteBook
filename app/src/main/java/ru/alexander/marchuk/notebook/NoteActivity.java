package ru.alexander.marchuk.notebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.UUID;

import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.dialog.AddingNoteDetailDialogFragment;
import ru.alexander.marchuk.notebook.dialog.EditNoteDetailDialogFragment;
import ru.alexander.marchuk.notebook.fragment.NoteDetailFragment;
import ru.alexander.marchuk.notebook.model.NoteDetailModel;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;

public class NoteActivity extends AppCompatActivity implements
        AddingNoteDetailDialogFragment.AddingNoteDetailListener,
        EditNoteDetailDialogFragment.EditingNoteDetailListener {

    public static final String EXTRA_NOTE_ID = "ru.alexander.marchuk.notebook.note_id";
    private FragmentManager fragmentManager;
    private NoteDetailFragment noteDetailFragment;
    NoteModel note;

    public static Intent newIntent(Context packegeContext, UUID id) {
        Intent intent = new Intent(packegeContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        noteDetailFragment = (NoteDetailFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if (noteDetailFragment == null) {

            UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);

            note = NoteModelLab.get(getApplicationContext()).getNote(
                    NoteBaseHelper.SELECTION_NOTE_UUID, new String[]{id.toString()}, null);

            noteDetailFragment = NoteDetailFragment.newInstance(note);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, noteDetailFragment)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingNoteDialogFragment = AddingNoteDetailDialogFragment.newInstance(note);
                addingNoteDialogFragment.show(fragmentManager, "AddingNoteDetailDialogFragment");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    @Override
    public void onNoteDetailAdded(NoteDetailModel newNoteDetail) {
        noteDetailFragment.addNoteDetail(newNoteDetail, true);
    }

    @Override
    public void onNoteDetailEdited(NoteDetailModel updateNoteDetail) {
        noteDetailFragment.updateNoteDetail(updateNoteDetail);
        NoteModelLab.get(getApplicationContext()).updateNoteDetail(updateNoteDetail);
    }
}