package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class CategoryViewModel extends BaseObservable
{
    @Bindable
    public String category;
    @Bindable
    public boolean showHeader = true;
    @Bindable
    public int permissionLevel = 0;
    @Bindable
    public String entityName;
}
