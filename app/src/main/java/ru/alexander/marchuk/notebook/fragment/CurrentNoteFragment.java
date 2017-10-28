package ru.alexander.marchuk.notebook.fragment;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.alexander.marchuk.notebook.adapter.CurrentNoteAdapter;
import ru.alexander.marchuk.notebook.adapter.NoteAdapter;
import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteTable;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;
import ru.alexander.marchuk.notebook.model.NoteSeparator;

public class CurrentNoteFragment extends NoteFragment {

    private  static CurrentNoteFragment sCurrentNoteFragment;

    public static CurrentNoteFragment newInstance(){
        if(sCurrentNoteFragment == null){
            sCurrentNoteFragment = new CurrentNoteFragment();
        }
        return sCurrentNoteFragment;
    }

    public CurrentNoteFragment() {
    }

    AddingNoteInDoneListener mAddingNoteInDoneListener;

    public interface AddingNoteInDoneListener {
        void onNoteAddedDone(NoteModel note);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAddingNoteInDoneListener = (AddingNoteInDoneListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddingNoteInDoneListener");
        }
    }

    @Override
    public void addNoteFromDB() {
        mAdapter.removeAllItem();
        List<NoteModel> notes = new ArrayList<>();
        notes.addAll(NoteModelLab.get(getActivity()).getNotes(NoteBaseHelper.SELECTION_NOTE_STATUS + " OR "
                + NoteBaseHelper.SELECTION_NOTE_STATUS, new String[]{Integer.toString(NoteModel.STATUS_CURRENT_NOTE),
                Integer.toString(NoteModel.STATUS_OVERDUE)}, NoteTable.Cols.DATE));

        for (int i = 0; i < notes.size(); i++) {
            addNote(notes.get(i), false);
        }
    }

    @Override
    protected NoteAdapter createAdapter() {
        return new CurrentNoteAdapter(this);
    }

    @Override
    public void addNote(NoteModel newNote, boolean saveToDB) {
        int position = -1;
        NoteSeparator separator = null;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).isNote()) {
                NoteModel note = (NoteModel) mAdapter.getItem(i);
                if (newNote.getDate().before(note.getDate())) {
                    position = i;
                    break;
                }
            }
        }

        if (newNote.getDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newNote.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newNote.setDateStatus(NoteSeparator.TYPE_OVERDUE);
                if (!mAdapter.containsSeparatorOverdue) {
                    mAdapter.containsSeparatorOverdue = true;
                    separator = new NoteSeparator(NoteSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newNote.setDateStatus(NoteSeparator.TYPE_TODAY);
                if (!mAdapter.containsSeparatorToday) {
                    mAdapter.containsSeparatorToday = true;
                    separator = new NoteSeparator(NoteSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newNote.setDateStatus(NoteSeparator.TYPE_TOMORROW);
                if (!mAdapter.containsSeparatorTomorrow) {
                    mAdapter.containsSeparatorTomorrow = true;
                    separator = new NoteSeparator(NoteSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newNote.setDateStatus(NoteSeparator.TYPE_TOMORROW);
                if (!mAdapter.containsSeparatorFuture) {
                    mAdapter.containsSeparatorFuture = true;
                    separator = new NoteSeparator(NoteSeparator.TYPE_FUTURE);
                }
            }
        }

        if (position != -1) {

            if (!mAdapter.getItem(position - 1).isNote()) {
                if (position - 2 >= 0 && mAdapter.getItem(position - 2).isNote()) {
                    NoteModel note = (NoteModel) mAdapter.getItem(position - 2);
                    if (note.getDateStatus() == newNote.getDateStatus()) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newNote.getDate() == null) {
                    position -= 1;
                }
            }

            if (separator != null) {
                mAdapter.addItem(position - 1, separator);
            }
            mAdapter.addItem(position, newNote);
        } else {
            if (separator != null) {
                mAdapter.addItem(separator);
            }
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
                NoteBaseHelper.SELECTION_NOTE_LIKE_TITLE + " AND " +
                        NoteBaseHelper.SELECTION_NOTE_STATUS + " OR " +
                        NoteBaseHelper.SELECTION_NOTE_STATUS,
                new String[]{
                        "%" + title + "%",
                        Integer.toString(NoteModel.STATUS_CURRENT_NOTE),
                        Integer.toString(NoteModel.STATUS_OVERDUE)
                },
                NoteTable.Cols.DATE));

        for (int i = 0; i < notes.size(); i++) {
            addNote(notes.get(i), false);
        }
    }

    @Override
    public void moveNote(NoteModel note) {
        mAddingNoteInDoneListener.onNoteAddedDone(note);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAddingNoteInDoneListener = null;
    }
}