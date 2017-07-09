package ru.alexander.marchuk.notebook.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.adapter.NoteAdapter;
import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteTable;
import ru.alexander.marchuk.notebook.dialog.EditNoteDialogFragment;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;
import ru.alexander.marchuk.notebook.model.Item;

public abstract class NoteFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected NoteAdapter mAdapter;

    protected abstract NoteAdapter createAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.note_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addNoteFromDB();
    }

    public abstract void addNote(NoteModel newNote, boolean saveToDB);

    public void updateNote(NoteModel note){
        mAdapter.updateItem(note);
    }

    public void removeNote(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_removing_message);

        Item item = mAdapter.getItem(position);

        if (item.isNote()) {
            NoteModel note = (NoteModel) item;
            final UUID uuid = note.getId();
            final boolean[] isRemoved = {false};

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAdapter.removeItem(position);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addNote(NoteModelLab.get(getActivity()).getNote(
                                    NoteBaseHelper.SELECTION_UUID,
                                    new String[]{String.valueOf(uuid)},
                                    NoteTable.Cols.DATE
                            ), false);
                            isRemoved[0] = false;
                        }
                    });

                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {

                            if (isRemoved[0]) {
                                NoteModelLab.get(getActivity()).removeNote(String.valueOf(uuid));
                            }

                        }
                    });

                    snackbar.show();

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        builder.show();

    }

    public void showNoteEditDialog(NoteModel note){
        DialogFragment editingNoteDialog = EditNoteDialogFragment.newInstance(note);
        editingNoteDialog.show(getActivity().getSupportFragmentManager(), "EditingNoteDialogFragment");
    }

    public abstract void findNote(String title);

    public abstract void addNoteFromDB();

    public abstract void moveNote(NoteModel note);
}
