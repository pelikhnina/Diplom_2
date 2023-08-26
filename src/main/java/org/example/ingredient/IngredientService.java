package org.example.ingredient;
import io.qameta.allure.Step;
import org.example.BaseService;

public class IngredientService extends BaseService {
    @Step
    public Ingredients getIngredientsApi(){
        return spec()
                .header("Content-type", "application/json")
                .get("/ingredients")
                .body()
                .as(Ingredients.class);
    }
}
