package ru.alexander.marchuk.notebook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.model.NoteDetailModel;

public class EditNoteDetailDialogFragment extends DialogFragment {

    private TextInputLayout mTilTitle;
    private EditText mEtTitle;

    private NoteDetailModel mNoteDetail;

    private String ARG_TITLE = "ru.alexander.marchuk.notebook.dialog.addingnotedialogfragment.title";

    private boolean bTitle;

    private static final String ARG_NOTE_DETAIL = "ru.alexander.marchuk.fitnessreminder.dialog.note_detail";

    private EditingNoteDetailListener mEditingNoteDetailListener;

    public interface EditingNoteDetailListener {
        void onNoteDetailEdited(NoteDetailModel updateNoteDetail);
    }

    public static EditNoteDetailDialogFragment newInstance(NoteDetailModel noteDetail) {
        EditNoteDetailDialogFragment editNoteDetailDialogFragment = new EditNoteDetailDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_DETAIL, noteDetail);
        editNoteDetailDialogFragment.setArguments(args);

        return editNoteDetailDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mEditingNoteDetailListener = (EditingNoteDetailListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditingNoteDetailListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteDetail = (NoteDetailModel) getArguments().getSerializable(ARG_NOTE_DETAIL);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_editing_title);

        View container = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_note_detail, null);

        mTilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTitle);
        mEtTitle = mTilTitle.getEditText();

        mEtTitle.setText(mNoteDetail.getTitle());

        mEtTitle.setSelection(mEtTitle.length());

        mTilTitle.setHint(getResources().getString(R.string.note_title));

        builder.setView(container);

        if (savedInstanceState != null) {
            mEtTitle.setText(savedInstanceState.getString(ARG_TITLE));
        }

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mEtTitle.length() != 0) {
                    mNoteDetail.setTitle(mEtTitle.getText().toString());
                }

                mEditingNoteDetailListener.onNoteDetailEdited(mNoteDetail);

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
                    bTitle = false;
                    mTilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                } else {
                    bTitle = true;
                }

                positiveButtonEnable(positiveButton);

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
            }
        });

        return alertDialog;
    }

    private void positiveButtonEnable(Button positiveButton) {
        if (bTitle) {
            positiveButton.setEnabled(true);
        } else {
            positiveButton.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TITLE, mEtTitle.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEditingNoteDetailListener = null;
    }
}