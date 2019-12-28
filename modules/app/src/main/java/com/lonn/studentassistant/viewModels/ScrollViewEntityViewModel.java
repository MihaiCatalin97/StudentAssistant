package com.lonn.studentassistant.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ScrollViewEntityViewModel extends BaseObservable {
    public Class entityActivityClass;
    @Bindable
    public int permissionLevel = 0;
    @Bindable
    public String field1, field2, field3, field4, image;

    public ScrollViewEntityViewModel(String image, String field1, String field2, String field3, String field4, Class entityActivityClass) {
        this.image = image;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.entityActivityClass = entityActivityClass;
    }

    public ScrollViewEntityViewModel(String image, String field1, String field2, String field3, Class entityActivityClass) {
        this.image = image;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.entityActivityClass = entityActivityClass;
    }

    @Bindable
    public int getField2LineCount() {
        if (field2 != null) {
            return field2.split("\n").length;
        }
        return 0;
    }

    @Bindable
    public int getField3LineCount() {
        if (field3 != null) {
            return field3.split("\n").length;
        }
        return 0;
    }

    @Bindable
    public int getField4LineCount() {
        if (field4 != null) {
            return field4.split("\n").length;
        }
        return 0;
    }
}
