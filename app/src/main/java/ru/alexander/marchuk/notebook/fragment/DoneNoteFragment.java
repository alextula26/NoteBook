package ru.alexander.marchuk.notebook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.notebook.adapter.NoteAdapter;
import ru.alexander.marchuk.notebook.adapter.DoneNoteAdapter;
import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteTable;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;

public class DoneNoteFragment extends NoteFragment {

    private static DoneNoteFragment sDoneNoteFragment;

    public static DoneNoteFragment newInstance(){
        if(sDoneNoteFragment == null){
            sDoneNoteFragment = new DoneNoteFragment();
        }
        return sDoneNoteFragment;
    }

    public DoneNoteFragment() {
//        Log.d("LOG", "DoneNoteFragment id = " + this.hashCode());
    }

    NoteRestoreListener mNoteRestoreListener;

    public interface NoteRestoreListener {
        void onNoteRestore(NoteModel note);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mNoteRestoreListener = (NoteRestoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoteRestoreListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("LOG", "onCreate DoneNoteFragment id = " + this.hashCode() + " activity id = " + getActivity().hashCode());
    }

    @Override
    protected NoteAdapter createAdapter() {
        return new DoneNoteAdapter(this);
    }

    @Override
    public void addNoteFromDB() {
        mAdapter.removeAllItem();
        List<NoteModel> notes = new ArrayList<>();
        notes.addAll(NoteModelLab.get(getActivity()).getNotes(NoteBaseHelper.SELECTION_NOTE_STATUS,
                new String[]{Integer.toString(NoteModel.STATUS_DONE_NOTE)}, NoteTable.Cols.DATE));

        for (int i = 0; i < notes.size(); i++) {
            addNote(notes.get(i), false);
        }
    }

    @Override
    public void addNote(NoteModel newNote, boolean saveToDB) {
        int position = -1;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).isNote()) {
                NoteModel note = (NoteModel) mAdapter.getItem(i);
                if (newNote.getDate().before(note.getDate())) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            mAdapter.addItem(position, newNote);
        } else {
            mAdapter.addItem(newNote);
        }

        if (saveToDB) {
            NoteModelLab.get(getActivity()).insertNote(newNote);
        }
    }

    @Override
    public void findNote(String title) {
        mAdapter.removeAllItem();
        List<NoteModel> notes = new ArrayList<>();
        notes.addAll(NoteModelLab.get(getActivity()).getNotes(
                NoteBaseHelper.SELECTION_NOTE_LIKE_TITLE + " AND " + NoteBaseHelper.SELECTION_NOTE_STATUS,
                new String[]{
                        "%" + title + "%",
                        Integer.toString(NoteModel.STATUS_DONE_NOTE)
                },
                NoteTable.Cols.DATE));

        for (int i = 0; i < notes.size(); i++) {
            addNote(notes.get(i), false);
        }
    }

    @Override
    public void moveNote(NoteModel note) {
        mNoteRestoreListener.onNoteRestore(note);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNoteRestoreListener = null;
    }
}