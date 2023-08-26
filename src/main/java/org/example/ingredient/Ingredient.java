package org.example.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    private String _id;
    private String name;

    public Ingredient() {

    }

    public String getId() {
        return _id;
    }
    public String getName() {
        return name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
