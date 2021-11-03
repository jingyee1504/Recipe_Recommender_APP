package com.example.reciperecommenderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciperecommenderapp.CookBook;
import com.example.reciperecommenderapp.CreateRecipe;
import com.example.reciperecommenderapp.R;
import com.example.reciperecommenderapp.RecipeAssistantClass;
import com.example.reciperecommenderapp.RecipeDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryRecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private Context context;
    private List<RecipeAssistantClass> recipeList;
    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference, mCountReference,mValueReference,mFavouriteReference;

    public CategoryRecipeAdapter(Context context, List<RecipeAssistantClass> recipeList) {
        this.context = context;
        this.recipeList = recipeList;

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("favouriteList");
        mCountReference = DATABASE.getReference("trendingCount");
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout_customize,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.RecipeTitles.setText(recipeList.get(position).getRecipeName());

        String imageUri = null;
        imageUri = recipeList.get(position).getImageURL();
        Picasso.get().load(imageUri).into(holder.RecipeImages);

        String recipeKey = recipeList.get(position).getKey();
        mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("key")
                .equalTo(recipeList.get(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    String key = childSnapshot.getKey();
                    if (recipeKey.equals(key)){
                        holder.btnFavourite.setBackgroundResource(R.drawable.ic_favorite_red);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAction(holder);
            }
        });

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

    private void performAction(RecipeViewHolder holder){
        mFavouriteReference = mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFavouriteReference.addListenerForSingleValueEvent(new ValueEventListener() {
                int check = 0;
                String postkey = recipeList.get(holder.getAdapterPosition()).getKey();
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dss : dataSnapshot.getChildren()){
                        //will get current user's all recipe in favourite list
                        String key = dss.getKey();
                        if (key.equals(postkey)){
                            check++;
                        }
                    }
                    if(check > 0){
                        deleteRecipeFavourite(holder);
                    }
                    else{
                        saveRecipeFavourite(holder);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    private void saveRecipeFavourite(RecipeViewHolder holder){

        String imgURL = recipeList.get(holder.getAdapterPosition()).getImageURL();
        String name = recipeList.get(holder.getAdapterPosition()).getRecipeName();
        String category = recipeList.get(holder.getAdapterPosition()).getCategory();
        String country = recipeList.get(holder.getAdapterPosition()).getCountry();
        String instructions = recipeList.get(holder.getAdapterPosition()).getInstructions();
        String ingredients = recipeList.get(holder.getAdapterPosition()).getIngredients();
        String key = recipeList.get(holder.getAdapterPosition()).getKey();

        RecipeAssistantClass assistantClass = new RecipeAssistantClass(name,ingredients,
                instructions,category,country,imgURL,key);
        holder.btnFavourite.setBackgroundResource(R.drawable.ic_favorite_red);

        mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(recipeList.get(holder.getAdapterPosition()).getKey())
                .setValue(assistantClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Recipe Added to Favourite Folder!",Toast.LENGTH_SHORT ).show();
                }
                else{
                    Toast.makeText(context,
                            "Added Recipe Failed! Retry...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteRecipeFavourite(RecipeViewHolder holder){
        holder.btnFavourite.setBackgroundResource(R.drawable.ic_favourite_red_border);

        String key = recipeList.get(holder.getAdapterPosition()).getKey();
        mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).removeValue();
    }

    private void getClickNumber(RecipeViewHolder holder){
        mValueReference = mCountReference.child(recipeList.get(holder.getAdapterPosition()).getKey()).child("count");
        mValueReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int num = dataSnapshot.getValue(Integer.class);

                mCountReference.child(recipeList.get(holder.getAdapterPosition()).getKey())
                        .child("count").setValue(num + 1);
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

class RecipeViewHolder extends RecyclerView.ViewHolder{

    ImageView RecipeImages;
    TextView RecipeTitles ;
    CardView RecipeCardView;
    Button btnFavourite;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        RecipeImages = itemView.findViewById(R.id.imgRecipe);
        RecipeTitles = itemView.findViewById(R.id.tvTitle);
        btnFavourite = itemView.findViewById(R.id.btnFav);
        RecipeCardView = itemView.findViewById(R.id.cvRecipe);
    }
}
