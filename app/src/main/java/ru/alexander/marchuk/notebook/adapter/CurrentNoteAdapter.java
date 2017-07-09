package ru.alexander.marchuk.notebook.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.Utils;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteModelLab;
import ru.alexander.marchuk.notebook.model.NoteSeparator;
import ru.alexander.marchuk.notebook.model.Item;
import ru.alexander.marchuk.notebook.fragment.CurrentNoteFragment;

public class CurrentNoteAdapter extends NoteAdapter {

    private static final int TYPE_NOTE = 0;
    private static final int TYPE_SEPARATOR = 1;

    public CurrentNoteAdapter(CurrentNoteFragment currentNoteFragment) {
        super(currentNoteFragment);
    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isNote()) {
            return TYPE_NOTE;
        } else {
            return TYPE_SEPARATOR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_NOTE:
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.recycler_item_note, parent, false);

                CardView container = (CardView) view.findViewById(R.id.container_item);
                LinearLayout contentDate = (LinearLayout) view.findViewById(R.id.container_item_date);
                TextView title = (TextView) view.findViewById(R.id.txt_title);
                TextView date = (TextView) view.findViewById(R.id.txt_date);
                TextView time = (TextView) view.findViewById(R.id.txt_time);
                ImageView popupmenu = (ImageView) view.findViewById(R.id.popupmenu);

                return new NoteViewHolder(view, container, contentDate, title, date, time, popupmenu);
            case TYPE_SEPARATOR:
                View separator = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.separator, parent, false);
                TextView type = (TextView) separator.findViewById(R.id.tvSeparatorName);

                return new SeparatorViewHolder(separator, type);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = mItems.get(position);

        if (item.isNote()) {
            holder.itemView.setEnabled(true);
            final NoteModel noteModel = (NoteModel) item;
            final NoteViewHolder noteViewHolder = (NoteViewHolder) holder;

            final View itemView = noteViewHolder.itemView;

            noteViewHolder.mContainerItem.setBackgroundColor(ContextCompat.getColor(mNoteFragment.getActivity(), R.color.container_even));
            noteViewHolder.mContainerItemDate.setBackgroundColor(ContextCompat.getColor(mNoteFragment.getActivity(), R.color.date_even));

//            if (position % 2 == 0) {
//                noteViewHolder.mLinearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mNoteFragment.getActivity(), R.color.container_even));
//                noteViewHolder.mLinearLayoutDate.setBackgroundColor(ContextCompat.getColor(mNoteFragment.getActivity(), R.color.date_even));
//            } else {
//                noteViewHolder.mLinearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mNoteFragment.getActivity(), R.color.container_uneven));
//                noteViewHolder.mLinearLayoutDate.setBackgroundColor(ContextCompat.getColor(mNoteFragment.getActivity(), R.color.date_uneven));
//            }

            noteViewHolder.mTitle.setText(noteModel.getTitle());
            noteViewHolder.mDate.setText(Utils.getFullDate(noteModel.getDate()));
            noteViewHolder.mTime.setText(Utils.getTime(noteModel.getTime()));

            itemView.setVisibility(View.VISIBLE);
            itemView.setEnabled(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveItemNote(noteViewHolder, itemView, noteModel);
                }
            });

            noteViewHolder.mPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getNoteFragment().getActivity(), noteViewHolder.mPopupMenu);
                    popupMenu.inflate(R.menu.popupmenu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.popupmenu_done:
                                    moveItemNote(noteViewHolder, itemView, noteModel);
                                    break;
                                case R.id.popupmenu_open:
                                    break;
                                case R.id.popupmenu_update:
                                    getNoteFragment().showNoteEditDialog(noteModel);
                                    break;
                                case R.id.popupmenu_delete:
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getNoteFragment().removeNote(noteViewHolder.getLayoutPosition());
                                        }
                                    }, 1000);
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

        } else {

            NoteSeparator separator = (NoteSeparator) item;
            SeparatorViewHolder separatorViewHolder = (SeparatorViewHolder) holder;

            separatorViewHolder.mType.setText(holder.itemView.getResources().getString(separator.getType()));
        }


    }

    private void moveItemNote(final NoteViewHolder noteViewHolder, final View itemView, final NoteModel noteModel){
        itemView.setEnabled(false);
        noteModel.setStatus(NoteModel.STATUS_DONE_NOTE);
        NoteModelLab.get(getNoteFragment().getActivity());
        if (noteModel.getStatus() == NoteModel.STATUS_DONE_NOTE) {
            ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                    "translationX", 0f, itemView.getWidth());

            ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                    "translationX", itemView.getWidth(), 0f);

            AnimatorSet translationSet = new AnimatorSet();
            translationSet.play(translationX).before(translationXBack);
            translationSet.start();

            translationX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    itemView.setVisibility(View.GONE);
                    // Получаем фрагмент и вызываем метод moveNote
                    getNoteFragment().moveNote(noteModel);
                    // Удаляем упражнение из фрагмента
                    removeItem(noteViewHolder.getLayoutPosition());

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
