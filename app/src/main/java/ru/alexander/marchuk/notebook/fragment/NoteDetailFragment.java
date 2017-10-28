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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.adapter.NoteDetailAdapter;

import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteDetailTable;
import ru.alexander.marchuk.notebook.dialog.EditNoteDetailDialogFragment;
import ru.alexander.marchuk.notebook.model.Item;
import ru.alexander.marchuk.notebook.model.NoteDetailModel;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;

public class NoteDetailFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected NoteDetailAdapter mAdapter;

    private static NoteDetailFragment sNoteDetailFragment;
    private NoteModel mNote;

    private static final String ARG_NOTE = "ru.alexander.marchuk.fitnessreminder.dialog.note";

    public NoteDetailFragment() {}

    public static NoteDetailFragment newInstance(NoteModel note){
        if(sNoteDetailFragment == null){
            sNoteDetailFragment = new NoteDetailFragment();
        }

        Bundle arg = new Bundle();
        arg.putSerializable(ARG_NOTE, note);
        sNoteDetailFragment.setArguments(arg);

        return sNoteDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNote = (NoteModel) getArguments().getSerializable(ARG_NOTE);
    }

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

        mAdapter = new NoteDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addNoteDetailFromDB();
    }

    public void addNoteDetailFromDB() {
        mAdapter.removeAllItem();

        List<NoteDetailModel> notesDetail = new ArrayList<>();
        notesDetail.addAll(NoteModelLab.get(getActivity()).getNotesDetail(NoteBaseHelper.SELECTION_NOTE_DETAIL_NOTEID,
                new String[]{mNote.getId().toString()}, NoteDetailTable.Cols.TITLE));

        for (int i = 0; i < notesDetail.size(); i++) {
            addNoteDetail(notesDetail.get(i), false);
        }
    }

    public void addNoteDetail(NoteDetailModel newNoteDetail, boolean saveToDB) {
        mAdapter.addItem(newNoteDetail);
        if (saveToDB) {
            NoteModelLab.get(getActivity()).insertNoteDetail(newNoteDetail);
        }
    }

    public void updateNoteDetail(NoteDetailModel noteDetail){
        mAdapter.updateItem(noteDetail);
    }

    public void completeItemNoteDetail(UUID uuid, boolean isChecked){
        NoteModelLab.get(getActivity()).updateNoteDetailStatus(
                uuid.toString(),
                Integer.toString(isChecked == true ? NoteDetailModel.STATUS_CURRENT_NOTE_DETAIL : NoteDetailModel.STATUS_DONE_NOTE_DETAIL)
        );
    }

    public void showNoteDetailEditDialog(NoteDetailModel noteDetail){
        DialogFragment editingNoteDetailDialog = EditNoteDetailDialogFragment.newInstance(noteDetail);
        editingNoteDetailDialog.show(getActivity().getSupportFragmentManager(), "EditingNoteDialogFragment");
    }

    public void removeNote(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_removing_message);

        Item item = mAdapter.getItem(position);

        if (item.isNote()) {
            NoteDetailModel noteDetail = (NoteDetailModel) item;
            final UUID uuid = noteDetail.getId();
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
                            addNoteDetail(NoteModelLab.get(getActivity()).getNoteDetail(
                                    NoteBaseHelper.SELECTION_NOTE_DETAIL_UUID,
                                    new String[]{uuid.toString()},
                                    NoteDetailTable.Cols.TITLE
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
                                NoteModelLab.get(getActivity()).removeNoteDetail(uuid.toString());
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
}