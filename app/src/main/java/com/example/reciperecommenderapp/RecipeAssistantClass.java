package com.example.reciperecommenderapp;

public class RecipeAssistantClass {

    String recipeName, ingredients, instructions, category, country,imageURL,key;

    public RecipeAssistantClass() {
    }

    public RecipeAssistantClass(String recipeName, String ingredients, String instructions, String category, String country, String imageURL,String key) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.country = country;
        this.imageURL = imageURL;
        this.key = key;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
