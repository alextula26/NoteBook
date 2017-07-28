package ru.alexander.marchuk.notebook.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.Date;

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.Utils;
import ru.alexander.marchuk.notebook.model.NoteModel;

public class EditNoteDialogFragment extends DialogFragment {

    private TextInputLayout mTilTitle;
    private EditText mEtTitle;
    private TextInputLayout mTilDate;
    private EditText mEtDate;
    private TextInputLayout mTilTime;
    private EditText mEtTime;

    private NoteModel mNote;

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_TIME = 1;

    private static final String ARG_NOTE = "ru.alexander.marchuk.fitnessreminder.dialog.note";

    private boolean bTitle = false;
    private boolean bDate = false;

    public static EditNoteDialogFragment newInstance(NoteModel note){
        EditNoteDialogFragment editNoteDialogFragment = new EditNoteDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE, (Serializable) note);
        editNoteDialogFragment.setArguments(args);

        return editNoteDialogFragment;
    }

    private EditingNoteListener mEditingNoteListener;

    public interface EditingNoteListener {
        void onNoteEdited(NoteModel updateNote);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mEditingNoteListener = (EditingNoteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddingNoteListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNote = (NoteModel) getArguments().getSerializable(ARG_NOTE);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_editing_title);

        View container = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_note, null);

        mTilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTitle);
        mEtTitle = mTilTitle.getEditText();

        mTilDate = (TextInputLayout) container.findViewById(R.id.tilDialogDate);
        mEtDate = mTilDate.getEditText();

        mTilTime = (TextInputLayout) container.findViewById(R.id.tilDialogTime);
        mEtTime = mTilTime.getEditText();

        mEtTitle.setText(mNote.getTitle());
        mEtDate.setText(Utils.getDate(mNote.getDate()));
        mEtTime.setText(Utils.getTime(mNote.getTime()));

        mEtTitle.setSelection(mEtTitle.length());

        mTilTitle.setHint(getResources().getString(R.string.note_title));
        mTilDate.setHint(getResources().getString(R.string.note_date));
        mTilTime.setHint(getResources().getString(R.string.note_time));

        builder.setView(container);

//        if(savedInstanceState != null){
//            etTitle.setText(savedInstanceState.getString(ARG_TITLE));
//            etDate.setText(savedInstanceState.getString(ARG_DATE));
//        }

        mEtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtDate.length() == 0) {
                    mEtDate.setText(" ");
                }

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mNote.getDate());
                datePickerFragment.setTargetFragment(EditNoteDialogFragment.this, REQUEST_DATE);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), DIALOG_DATE);
            }
        });

        mEtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mNote.getTime());
                timePickerFragment.setTargetFragment(EditNoteDialogFragment.this, REQUEST_TIME);
                timePickerFragment.show(getActivity().getSupportFragmentManager(), DIALOG_TIME);
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mEtTitle.length() != 0) {
                    mNote.setTitle(mEtTitle.getText().toString());
                }

                if (mEtDate.length() != 0) {
                    mNote.setDate(Utils.parseDate(mEtDate.getText().toString()));
                }

                if(mEtTime.length() != 0){
                    mNote.setTime(Utils.parseTime(mEtTime.getText().toString()));
                }

                mNote.setStatus(NoteModel.STATUS_CURRENT_NOTE);
                mEditingNoteListener.onNoteEdited(mNote);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (mEtTitle.length() == 0) {
                    positiveButton.setEnabled(false);
                    mTilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }

                mEtTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.length() == 0) {
                            bTitle = false;
                            mTilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        } else {
                            bTitle = true;
                            mTilTitle.setErrorEnabled(false);
                        }

                        positiveButtonEnable(positiveButton);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                if (mEtDate.length() == 0) {
                    positiveButton.setEnabled(false);
                    mTilDate.setError(getResources().getString(R.string.dialog_error_empty_date));
                }

                mEtDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (s.length() == 0) {
                            bDate = false;
                            mTilDate.setError(getResources().getString(R.string.dialog_error_empty_title));
                        } else {
                            bDate = true;
                            mTilDate.setErrorEnabled(false);
                        }

                        positiveButtonEnable(positiveButton);

                    }
                });
            }
        });

        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            if (date != null) {
                mEtDate.setText(Utils.getDate(date));
            }
        }

        if(requestCode == REQUEST_TIME){
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            if(time != null){
                mEtTime.setText(Utils.getTime(time));
            }
        }
    }

    private void positiveButtonEnable(Button positiveButton){
        if(bTitle && bDate){
            positiveButton.setEnabled(true);
        }else{
            positiveButton.setEnabled(false);
        }

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putString(ARG_TITLE, etTitle.getText().toString());
//        outState.putString(ARG_DATE, etDate.getText().toString());
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEditingNoteListener = null;
    }
}