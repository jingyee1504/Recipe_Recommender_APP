package com.example.reciperecommenderapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciperecommenderapp.CookBook;
import com.example.reciperecommenderapp.CreateRecipe;
import com.example.reciperecommenderapp.EditRecipe;
import com.example.reciperecommenderapp.R;
import com.example.reciperecommenderapp.RecipeAssistantClass;
import com.example.reciperecommenderapp.RecipeDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CookbookAdapter extends RecyclerView.Adapter<CookbookRecipeViewHolder> {
    private Context context;
    private List<RecipeAssistantClass> recipeList;
    FirebaseDatabase DATABASE;
    FirebaseStorage mFirebaseStorage;
    DatabaseReference mDatabaseReference, mDeleteDatabaseReference, mCountReference,mValueReference, mFavouriteReference;
    StorageReference mStorageReference;

    public CookbookAdapter(Context context, List<RecipeAssistantClass> recipeList) {
        this.context = context;
        this.recipeList = recipeList;

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("cookbook");
        mDeleteDatabaseReference = DATABASE.getReference("recipes");
        mCountReference = DATABASE.getReference("trendingCount");
        mFavouriteReference = DATABASE.getReference("favouriteList");
        mFirebaseStorage = FirebaseStorage.getInstance("gs://recipe-recommender-app-77e1b.appspot.com");

    }

    @NonNull
    @Override
    public CookbookRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_cookbook_customize,parent,false);
        return new CookbookRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CookbookRecipeViewHolder holder, int position) {
        holder.RecipeTitles.setText(recipeList.get(position).getRecipeName());

        String imageUri = null;
        imageUri = recipeList.get(position).getImageURL();
        Picasso.get().load(imageUri).into(holder.RecipeImages);

        holder.btnRecipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{
                        "Delete",
                        "Cancel"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Recipe");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //if delete option is chosen
                        if(i == 0){
                            mStorageReference = mFirebaseStorage.getReferenceFromUrl(recipeList.get(holder.getAdapterPosition()).getImageURL());
                            mStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(recipeList.get(holder.getAdapterPosition()).getKey()).removeValue();
                                    mDeleteDatabaseReference.child(recipeList.get(holder.getAdapterPosition()).getKey()).removeValue();
                                    mCountReference.child(recipeList.get(holder.getAdapterPosition()).getKey()).removeValue();

                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Recipe Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                builder.show();
            }
        });

        holder.RecipeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{
                        "View",
                        "Edit"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("View/Edit ?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //if View option is chosen
                        if(i == 0){
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
                        else{
                            Intent intent = new Intent(context, EditRecipe.class);
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
                    }
                });
                builder.show();
            }
        });
    }

    private void getClickNumber(CookbookRecipeViewHolder holder){
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

class CookbookRecipeViewHolder extends RecyclerView.ViewHolder{

    ImageView RecipeImages;
    TextView RecipeTitles ;
    Button btnRecipeDelete;
    CardView RecipeCardView;

    public CookbookRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        RecipeImages = itemView.findViewById(R.id.imgRecipe);
        RecipeTitles = itemView.findViewById(R.id.tvTitle);
        btnRecipeDelete = itemView.findViewById(R.id.btnDelete);
        RecipeCardView = itemView.findViewById(R.id.cvRecipe);
    }
}
