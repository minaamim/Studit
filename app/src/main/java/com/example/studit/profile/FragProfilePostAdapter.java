package com.example.studit.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studit.R;

import java.util.ArrayList;

public class FragProfilePostAdapter extends RecyclerView.Adapter<FragProfilePostAdapter.FragProfilePostViewHolder> {
    private final ArrayList<FragProfilePostModel> PostModelArrayList;
    private Context context;
    String getContentsNum;
    int pos;

    public FragProfilePostAdapter(ArrayList<FragProfilePostModel> PostModelArrayList, Context context) {
        this.PostModelArrayList = PostModelArrayList;
        this.context = context;
    }



    public class FragProfilePostViewHolder extends RecyclerView.ViewHolder {
        public TextView field,title,content,date;

        public FragProfilePostViewHolder(View view) {
            super(view);
            this.field = view.findViewById(R.id.postCategory);
            this.title = view.findViewById(R.id.postTitle);
            this.content = view.findViewById(R.id.postContent);
            this.date = view.findViewById(R.id.postDate);

            };
        }


    @NonNull
    @Override
    public FragProfilePostAdapter.FragProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_frag_profile_timeline, parent, false);
        FragProfilePostAdapter.FragProfilePostViewHolder holder = new FragProfilePostAdapter.FragProfilePostViewHolder(cardView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FragProfilePostAdapter.FragProfilePostViewHolder holder, int position) {

        FragProfilePostModel dataModelPosition = PostModelArrayList.get(position);
        holder.field.setText(dataModelPosition.getField());
        holder.title.setText(dataModelPosition.getTitle());
        holder.content.setText(dataModelPosition.getContent());
        holder.date.setText(dataModelPosition.getDate());

        context = holder.itemView.getContext();

        /* 리사이클러뷰의 버튼을 클릭할 때 실행될 것들을 적어준다. */
        //holder.title.setOnClickListener(v -> Toast.makeText(context, "리사이클러뷰의 제목이 클릭되었습니다.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return (PostModelArrayList != null ? PostModelArrayList.size() : 0);
    }
}

