package ru.alexander.marchuk.notebook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.adapter.NoteDetailAdapter;

import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.model.NoteDetailModel;
import ru.alexander.marchuk.notebook.model.NoteDetailModelLab;
import ru.alexander.marchuk.notebook.model.NoteModel;

public class NoteDetailFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected NoteDetailAdapter mAdapter;

    private static NoteDetailFragment sNoteDetailFragment;
    private NoteModel mNote;

    private static final String ARG_NOTE = "ru.alexander.marchuk.fitnessreminder.dialog.note";

    public NoteDetailFragment() {
    }

    public static NoteDetailFragment newInstance(NoteModel note){
        if(sNoteDetailFragment == null){
            sNoteDetailFragment = new NoteDetailFragment();
            Bundle arg = new Bundle();
            arg.putSerializable(ARG_NOTE, note);
            sNoteDetailFragment.setArguments(arg);

        }
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

        Toast.makeText(getActivity(), "title = " + mNote.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //addNoteFromDB();
    }

    public void addNoteFromDB() {
        mAdapter.removeAllItem();
        List<NoteDetailModel> notesDetail = new ArrayList<>();
        notesDetail.addAll(NoteDetailModelLab.get(getActivity()).getNotesDetail(NoteBaseHelper.SELECTION_NOTEID,
                new String[]{Long.toString(1)}, null));
    }

    public void addNote(NoteDetailModel newNote, boolean saveToDB) {

            mAdapter.addItem(newNote);



    }
}
