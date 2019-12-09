package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class CategoryViewModel extends BaseObservable {
    @Bindable
    public String categoryName;
    @Bindable
    public boolean showHeader = true;
    @Bindable
    public int permissionLevel = 0;
    @Bindable
    public String entityName;
    @Bindable
    public boolean isEndCategory = true;
    @Bindable
    public String viewType = "full";
    @Bindable
    public boolean showEmpty = true;
    @Bindable
    public String childCategoryTypes = "none";
}
