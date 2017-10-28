package ru.alexander.marchuk.notebook.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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

    public void addItem(Item item) {
        mItems.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void updateItem(NoteDetailModel newNoteDetail){
        for(int i = 0; i < getItemCount(); i++){
            if(getItem(i).isNote()){
                NoteDetailModel noteDetail = (NoteDetailModel) getItem(i);
                if(newNoteDetail.getId() == noteDetail.getId()){
                    removeItem(i);
                    getNoteDetailFragment().addNoteDetail(newNoteDetail, false);
                }
            }
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
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

        TextView title = (TextView) view.findViewById(R.id.txt_title);
        Switch switcher = (Switch) view.findViewById(R.id.btn_switch);
        ImageView edit = (ImageView) view.findViewById(R.id.img_edit);
        ImageView delete = (ImageView) view.findViewById(R.id.img_delete);

        return new NoteDetailViewHolder(view, title, switcher, edit, delete);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = mItems.get(position);

        if (item.isNote()) {
            holder.itemView.setEnabled(true);
            final NoteDetailModel noteDetailModel = (NoteDetailModel) item;
            final NoteDetailViewHolder noteDetailViewHolder = (NoteDetailViewHolder) holder;

            final View itemView = noteDetailViewHolder.itemView;

            noteDetailViewHolder.mTitle.setText(noteDetailModel.getTitle());
            noteDetailViewHolder.mSwitch.setChecked(noteDetailModel.getStatus() == NoteDetailModel.STATUS_CURRENT_NOTE_DETAIL ? true : false);

            noteDetailViewHolder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getNoteDetailFragment().completeItemNoteDetail(noteDetailModel.getId(), isChecked);
                }
            });

            itemView.setVisibility(View.VISIBLE);
            itemView.setEnabled(true);

            noteDetailViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNoteDetailFragment().showNoteDetailEditDialog(noteDetailModel);
                }
            });

            noteDetailViewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getNoteDetailFragment().removeNote(noteDetailViewHolder.getLayoutPosition());
                        }
                    }, 1000);
                }
            });

        }
    }

    protected class NoteDetailViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTitle;
        protected Switch mSwitch;
        protected ImageView mEdit;
        protected ImageView mDelete;

        public NoteDetailViewHolder(
                View itemView,
                TextView title,
                Switch switcher,
                ImageView edit,
                ImageView delete
        ){
            super(itemView);
            mTitle = title;
            mSwitch = switcher;
            mEdit = edit;
            mDelete = delete;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public NoteDetailFragment getNoteDetailFragment() {
        return mNoteDetailFragment;
    }

}