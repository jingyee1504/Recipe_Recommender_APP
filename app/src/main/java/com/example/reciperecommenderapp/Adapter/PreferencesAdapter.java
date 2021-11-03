package com.example.reciperecommenderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciperecommenderapp.R;
import com.example.reciperecommenderapp.RecipeAssistantClass;
import com.example.reciperecommenderapp.RecipeDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesViewHolder> {
    private Context context;
    private List<RecipeAssistantClass> recipeList;
    FirebaseDatabase DATABASE;
    DatabaseReference mCountReference,mValueReference;

    public PreferencesAdapter(Context context, List<RecipeAssistantClass> recipeList) {
        this.context = context;
        this.recipeList = recipeList;

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mCountReference = DATABASE.getReference("trendingCount");
    }

    @NonNull
    @Override
    public PreferencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_preferences_customize,parent,false);
        return new PreferencesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferencesViewHolder holder, int position) {
        holder.RecipeTitles.setText(recipeList.get(position).getRecipeName());

        String imageUri = null;
        imageUri = recipeList.get(position).getImageURL();
        Picasso.get().load(imageUri).into(holder.RecipeImages);

        holder.RecipeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClickNumber(holder);

                Intent intent = new Intent(context, RecipeDetails.class);
                intent.putExtra("Image",recipeList.get(holder.getAdapterPosition()).getImageURL());
                intent.putExtra("Title",recipeList.get(holder.getAdapterPosition()).getRecipeName());
                intent.putExtra("Category",recipeList.get(holder.getAdapterPosition()).getCategory());
                intent.putExtra("Country",recipeList.get(holder.getAdapterPosition()).getCountry());
                intent.putExtra("Instructions",recipeList.get(holder.getAdapterPosition()).getInstructions());
                intent.putExtra("Ingredients",recipeList.get(holder.getAdapterPosition()).getIngredients());
                intent.putExtra("key",recipeList.get(holder.getAdapterPosition()).getKey());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void getClickNumber(PreferencesViewHolder holder){
        mValueReference = mCountReference.child(recipeList.get(holder.getAdapterPosition()).getKey()).child("count");
        mValueReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int num = dataSnapshot.getValue(Integer.class);

                mCountReference.child(recipeList.get(holder.getAdapterPosition()).getKey()).child("count")
                        .setValue(num + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}

class PreferencesViewHolder extends RecyclerView.ViewHolder{

    ImageView RecipeImages;
    TextView RecipeTitles ;
    CardView RecipeCardView;

    public PreferencesViewHolder(@NonNull View itemView) {
        super(itemView);

        RecipeImages = itemView.findViewById(R.id.imgRecipe);
        RecipeTitles = itemView.findViewById(R.id.tvTitle);

        RecipeCardView = itemView.findViewById(R.id.cvRecipe);
    }
}

