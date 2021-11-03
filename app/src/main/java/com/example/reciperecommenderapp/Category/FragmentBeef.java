package com.example.reciperecommenderapp.Category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.reciperecommenderapp.Adapter.CategoryRecipeAdapter;
import com.example.reciperecommenderapp.Adapter.CookbookAdapter;
import com.example.reciperecommenderapp.Adapter.FragmentAdapter;
import com.example.reciperecommenderapp.R;
import com.example.reciperecommenderapp.RecipeAssistantClass;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentBeef extends Fragment {

    FirebaseDatabase DATABASE;
    DatabaseReference mDatabaseReference;

    int numCategory = 12;
    int val;

    RecyclerView recipeRecyclerView;
    List<RecipeAssistantClass> recipeList;
    CategoryRecipeAdapter mCategoryRecipeAdapter;
    TextView tvCategory,tvDescription;
    ImageView imgCategory,imageCategoryBg;
    String[] categoryArray;
    ProgressBar loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_beef, container, false);

        // Inflate the layout for this fragment // Use this if clash

        recipeRecyclerView = rootView.findViewById(R.id.recyclerView2);
        tvCategory = rootView.findViewById(R.id.textCategory);
        tvDescription = rootView.findViewById(R.id.textDescription);
        imgCategory  = rootView.findViewById(R.id.imageCategory);
        imageCategoryBg = rootView.findViewById(R.id.imageCategoryBg);
        loadingBar = rootView.findViewById(R.id.loadingBar);
        categoryArray = getResources().getStringArray(R.array.category);

        recipeList = new ArrayList<>();
        mCategoryRecipeAdapter = new CategoryRecipeAdapter(getContext(),recipeList);

        //SpanCount - Split into 2 columns
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);

        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(mGridLayoutManager);
        recipeRecyclerView.setAdapter(mCategoryRecipeAdapter);

        //initViews(rootView);
        //get the dynamically generated fragment position
        val = getArguments().getInt("someInt", 0);

        DATABASE = FirebaseDatabase.getInstance("https://recipe-recommender-app-77e1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = DATABASE.getReference("recipes");
        Query checkCategory;

        //Set the Meal Category (Title, image, background image) with particular fragment position
        switch (val){
            case 0 :
                tvCategory.setText("Beef");
                tvDescription.setText("Beef, flesh of mature cattle,as distinguished from veal.");
                imgCategory.setImageResource(R.drawable.beef);
                imageCategoryBg.setImageResource(R.drawable.beef);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Beef");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 1 :
                tvCategory.setText("Chicken");
                tvDescription.setText("Chicken is a staple in most home kitchens, and with good reason.");
                imgCategory.setImageResource(R.drawable.chicken);
                imageCategoryBg.setImageResource(R.drawable.chicken);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Chicken");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 2 :
                tvCategory.setText("Dessert");
                tvDescription.setText("Dessert is a course that concludes a meal with sweet food.");
                imgCategory.setImageResource(R.drawable.dessert);
                imageCategoryBg.setImageResource(R.drawable.dessert);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Dessert");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 3 :
                tvCategory.setText("Fish");
                tvDescription.setText("Fish is healthy and easy to bake, grill, or fry.");
                imgCategory.setImageResource(R.drawable.fish);
                imageCategoryBg.setImageResource(R.drawable.fish);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Fish");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 4 :
                tvCategory.setText("Pasta");
                tvDescription.setText("Pasta is a food made from wheat flour and water.");
                imgCategory.setImageResource(R.drawable.pasta);
                imageCategoryBg.setImageResource(R.drawable.pasta);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Pasta");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 5 :
                tvCategory.setText("Seafood");
                tvDescription.setText("Seafood is any form of sea life regarded as food by humans.");
                imgCategory.setImageResource(R.drawable.seafood);
                imageCategoryBg.setImageResource(R.drawable.seafood);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Seafood");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 6 :
                tvCategory.setText("Sides");
                tvDescription.setText("A side dish is essentially any food served to the side of the main plate.");
                imgCategory.setImageResource(R.drawable.sides);
                imageCategoryBg.setImageResource(R.drawable.sides);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Sides");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 7 :
                tvCategory.setText("Sushi");
                tvDescription.setText("Sushi, a staple rice dish of Japanese cuisine that served cold");
                imgCategory.setImageResource(R.drawable.sushi);
                imageCategoryBg.setImageResource(R.drawable.sushi);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Sushi");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 8 :
                tvCategory.setText("Vegan");
                tvDescription.setText("Plant-based diets are healthier,and environment-friendly.");
                imgCategory.setImageResource(R.drawable.vegan);
                imageCategoryBg.setImageResource(R.drawable.vegan);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Vegan");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 9 :
                tvCategory.setText("Noodles");
                tvDescription.setText("Noodle are  the most important traditional stale foods in Asia.");
                imageCategoryBg.setImageResource(R.drawable.noodle);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Noodles");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 10 :
                tvCategory.setText("Western");
                tvDescription.setText("Western Cuisine centers meals around a meat dish.");
                imgCategory.setImageResource(R.drawable.burger);
                imageCategoryBg.setImageResource(R.drawable.burger);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Western");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 11:
                tvCategory.setText("Broth");
                tvDescription.setText("Broth is made by simmering meat in water with vegetables,herbs.");
                imgCategory.setImageResource(R.drawable.broth);
                imageCategoryBg.setImageResource(R.drawable.broth);

                checkCategory = mDatabaseReference.orderByChild("category").equalTo("Broth");
                checkCategory.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //mDatabaseReference.orderByChild("category").equalTo("Beef");
                        RecipeAssistantClass recipeModel = dataSnapshot.getValue(RecipeAssistantClass.class);
                        recipeModel.setKey(dataSnapshot.getKey());
                        recipeList.add(recipeModel);
                        mCategoryRecipeAdapter.notifyDataSetChanged();
                        loadingBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
        }

        return rootView;
    }

    //Adding the fragment with particular arguments
    public static FragmentBeef addfrag(int val) {
        FragmentBeef frag = new FragmentBeef();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        frag.setArguments(args);
        return frag;
    }
}
