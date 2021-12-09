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

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter01 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ITEM = 0;
    private final int TYPE_LOADING = 1;
    private boolean isLoading = false;


    List<NoteEntity> listNote;
    NoteClickDeleteInterface noteClickDeleteInterface;
    NoteClickInterface noteClickInterface;

    public NoteAdapter01() {
        this.listNote = new ArrayList<>();
    }

    //Nên viết thêm: type: 1.change, 2.insert, 3.remove
    public void updateListNote(List<NoteEntity> listNoteEntity,List<NoteEntity> addedList ) {
        if (listNote.size() > 0) {
            listNote.clear();
        }

        listNote.addAll(listNoteEntity);
        if(addedList==null){
            notifyItemInserted(listNote.size() - 1);
        }else{
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == listNote.size() - 1 && listNote.get(listNote.size() - 1)==null ) {
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
        if (holder.getItemViewType() == TYPE_ITEM) {
            ((NoteViewHolder) holder).onBindView(listNote.get(position), position);
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
                        noteClickDeleteInterface.onDeleteIconClick(getAdapterPosition(), listNote.get(getAdapterPosition()));
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
            tvTitle.setText(noteEntity.getTitle() + " - id: " + noteEntity.getId());
            tvDescription.setText(noteEntity.getDescription());
            tvTime.setText(noteEntity.getTimestamp());
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBarLoading;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBarLoading = itemView.findViewById(R.id.progressbarLoading);
        }
    }

    public void setOnDeleteItemListener(NoteClickDeleteInterface noteClickDeleteInterface) {
        this.noteClickDeleteInterface = noteClickDeleteInterface;
    }

    public void setOnClickItemListener(NoteClickInterface noteClickInterface) {
        this.noteClickInterface = noteClickInterface;
    }

    public void addFooterLoading() {
        isLoading = true;
        listNote.add(null);
        notifyItemInserted(listNote.size() - 1);

    }
    //Nếu có item_loading thì xóa
    public void removeLoading() {
        if(listNote.size() > 0 ){
            int position = listNote.size() - 1;
            if (listNote.get(position) == null) {
                isLoading = false;

                listNote.remove(position);
//            notifyItemRemoved(position);
            }
        }


    }
}
