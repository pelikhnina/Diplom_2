package org.example.ingredient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredients {
    private String success;
    private List<Ingredient> data;

    public Ingredients() {

    }

    public List<Ingredient> getIngredients() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
