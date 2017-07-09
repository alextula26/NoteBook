package ru.alexander.marchuk.notebook.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.notebook.fragment.NoteFragment;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteSeparator;
import ru.alexander.marchuk.notebook.model.Item;

public abstract class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> mItems;
    NoteFragment mNoteFragment;

    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    public boolean containsSeparatorFuture;

    public NoteAdapter(NoteFragment noteFragment) {
        mNoteFragment = noteFragment;
        mItems = new ArrayList<>();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    // Добавление пункта в конец списка
    public void addItem(Item item) {
        mItems.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    // Добавление пункта в определенную позицию списка
    public void addItem(int position, Item item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void updateItem(NoteModel newNote){
        for(int i = 0; i < getItemCount(); i++){
            if(getItem(i).isNote()){
                NoteModel note = (NoteModel) getItem(i);
                if(newNote.getId() == note.getId()){
                    removeItem(i);
                    getNoteFragment().addNote(newNote, false);
                }
            }
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            mItems.remove(position);
            notifyItemRemoved(position);
            if (position - 1 >= 0 && position <= getItemCount() - 1) {
                if (!getItem(position).isNote() && !getItem(position - 1).isNote()) {
                    NoteSeparator separator = (NoteSeparator) getItem(position - 1);
                    checkSeparators(separator.getType());
                    mItems.remove(position - 1);
                    notifyItemRemoved(position - 1);
                }
            } else if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isNote()) {
                NoteSeparator separator = (NoteSeparator) getItem(getItemCount() - 1);
                checkSeparators(separator.getType());

                int positionTemp = getItemCount() - 1;
                mItems.remove(positionTemp);
                notifyItemRemoved(positionTemp);
            }
        }
    }

    public void checkSeparators(int type) {
        switch (type) {
            case NoteSeparator.TYPE_OVERDUE:
                containsSeparatorOverdue = false;
                break;
            case NoteSeparator.TYPE_TODAY:
                containsSeparatorToday = false;
                break;
            case NoteSeparator.TYPE_TOMORROW:
                containsSeparatorTomorrow = false;
                break;
            case NoteSeparator.TYPE_FUTURE:
                containsSeparatorFuture = false;
                break;
        }
    }

    public void removeAllItem() {
        if (getItemCount() != 0) {
            mItems = new ArrayList<>();
            notifyDataSetChanged();
            containsSeparatorOverdue = false;
            containsSeparatorToday = false;
            containsSeparatorTomorrow = false;
            containsSeparatorFuture = false;
        }
    }

    protected class NoteViewHolder extends RecyclerView.ViewHolder {

        protected CardView mContainerItem;
        protected LinearLayout mContainerItemDate;
        protected TextView mTitle;
        protected TextView mDate;
        protected TextView mTime;
        protected ImageView mPopupMenu;

        public NoteViewHolder(
                View itemView,
                CardView container,
                LinearLayout contentDate,
                TextView title,
                TextView date,
                TextView time,
                ImageView popupmenu
        ){
            super(itemView);
            mContainerItem = container;
            mContainerItemDate = contentDate;
            mTitle = title;
            mDate = date;
            mTime = time;
            mPopupMenu = popupmenu;
        }
    }

    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {

        protected TextView mType;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            mType = type;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public NoteFragment getNoteFragment() {
        return mNoteFragment;
    }
}
