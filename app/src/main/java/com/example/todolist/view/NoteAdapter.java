package com.example.todolist.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.todolist.R;
import com.example.todolist.database.NoteEntity;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 0;
    private final int TYPE_LOADING = 1;
    private boolean isLoading = false;


    List<NoteEntity> listNote;
    NoteClickDeleteInterface noteClickDeleteInterface;
    NoteClickInterface noteClickInterface;

    public NoteAdapter(List<NoteEntity> listNote) {
        this.listNote = listNote;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listNote.size() - 1 && isLoading) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khai báo lớp đối tượng để convert kiểu int sang kiểu view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (TYPE_ITEM == viewType) {
            View view = layoutInflater.inflate(R.layout.item_note, parent, false);
            return new NoteViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            ((NoteViewHolder)holder).onBindView(listNote.get(position) , position);
        }
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvTime;
        ImageView imgDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvDescription = itemView.findViewById(R.id.textViewDescription);
            tvTime = itemView.findViewById(R.id.textViewTime);
            imgDelete = itemView.findViewById(R.id.imageViewDelete);
            //Click vào hình Delete
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noteClickDeleteInterface != null) {
                        noteClickDeleteInterface.onDeleteIconClick(listNote.get(getAdapterPosition()));
                    }
                }
            });
            //Click vào thẻ
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteClickInterface.onNoteClick(listNote.get(getAdapterPosition()));
                }
            });
        }

        public void onBindView(NoteEntity noteEntity, int position) {
            tvTitle.setText(noteEntity.getTitle());
            tvDescription.setText(noteEntity.getDescription());
            tvTime.setText(noteEntity.getTimestamp());
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBarLoading;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBarLoading = itemView.findViewById(R.id.progressbarLoading);
        }
    }

    public void updateList(List<NoteEntity> newList){
        // on below line we are clearing
        // our notes array list
        listNote.clear();
        // on below line we are adding a
        // new list to our all notes list.
        listNote.addAll(newList);
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged();
    }

    public void setOnDeleteItemListener(NoteClickDeleteInterface noteClickDeleteInterface){
        this.noteClickDeleteInterface = noteClickDeleteInterface;
    }

    public void setOnClickItemListener(NoteClickInterface noteClickInterface){
        this.noteClickInterface =noteClickInterface;
    }

    public void addFooterLoading(){
        isLoading = true;
        listNote.add(null);
    }

    public void removeLoading(){
        isLoading = false;
        int position = listNote.size() - 1;
        listNote.remove(position);
        notifyItemRemoved(position);
    }
}
