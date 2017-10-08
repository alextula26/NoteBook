package ru.alexander.marchuk.notebook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.notebook.R;
import ru.alexander.marchuk.notebook.fragment.NoteDetailFragment;
import ru.alexander.marchuk.notebook.model.Item;
import ru.alexander.marchuk.notebook.model.NoteDetailModel;

public class NoteDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> mItems;
    NoteDetailFragment mNoteDetailFragment;

   public NoteDetailAdapter(NoteDetailFragment noteDetailFragment) {
        mNoteDetailFragment = noteDetailFragment;
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

    public void removeAllItem() {
        if (getItemCount() != 0) {
            mItems = new ArrayList<>();
            notifyDataSetChanged();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_note_recycler_item_detail, parent, false);

        TextView title = (TextView) view.findViewById(R.id.name_b);

        return new NoteDetailViewHolder(view, title);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = mItems.get(position);

        if (item.isNote()) {
            holder.itemView.setEnabled(true);
            final NoteDetailModel noteModel = (NoteDetailModel) item;
            final NoteDetailViewHolder noteDetailViewHolder = (NoteDetailViewHolder) holder;

            final View itemView = noteDetailViewHolder.itemView;

            noteDetailViewHolder.mTitle.setText(noteModel.getTitle());

            itemView.setVisibility(View.VISIBLE);
            itemView.setEnabled(true);
        }
    }

    protected class NoteDetailViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTitle;

        public NoteDetailViewHolder(
                View itemView,
                TextView title
        ){
            super(itemView);
            mTitle = title;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public NoteDetailFragment getNoteFragment() {
        return mNoteDetailFragment;
    }
}