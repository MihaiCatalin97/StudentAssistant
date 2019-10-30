package com.lonn.studentassistant.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class CategoryViewModel extends BaseObservable {
    @Bindable
    public String category;
    @Bindable
    public boolean showHeader = true;
    @Bindable
    public int permissionLevel = 0;
    @Bindable
    public String entityName;
}
