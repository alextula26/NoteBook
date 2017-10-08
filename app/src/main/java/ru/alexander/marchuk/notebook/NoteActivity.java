package ru.alexander.marchuk.notebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.UUID;

import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.fragment.NoteDetailFragment;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;

public class NoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "ru.alexander.marchuk.notebook.note_id";

    public static Intent newIntent(Context packegeContext, UUID id){
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

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){

            UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);

            NoteModel note = NoteModelLab.get(this).getNote(
                    NoteBaseHelper.SELECTION_UUID, new String[]{String.valueOf(id)}, null);

            fragment = NoteDetailFragment.newInstance(note);
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
}
