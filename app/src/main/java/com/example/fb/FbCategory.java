package com.example.fb;

import java.io.Serializable;

/** Model Firebase: node 'categories' */
public class FbCategory implements Serializable {
    private String key;          // CAT001, CAT002...
    private String categoryName;
    private String description;

    public FbCategory() {}

    public FbCategory(String key, String categoryName, String description) {
        this.key = key;
        this.categoryName = categoryName;
        this.description = description;
    }

    public String getKey()                      { return key; }
    public void   setKey(String key)            { this.key = key; }
    public String getCategoryName()             { return categoryName; }
    public void   setCategoryName(String n)     { this.categoryName = n; }
    public String getDescription()              { return description; }
    public void   setDescription(String d)      { this.description = d; }
}
