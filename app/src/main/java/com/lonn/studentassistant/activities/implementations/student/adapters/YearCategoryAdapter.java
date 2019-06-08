package com.lonn.studentassistant.activities.implementations.student.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lonn.studentassistant.views.implementations.CourseYearCategory;

import java.util.LinkedList;
import java.util.List;

public class YearCategoryAdapter extends ArrayAdapter<Integer>
{
    public List<CourseYearCategory> categories = new LinkedList<>();

    public YearCategoryAdapter(Context context, Integer[] categories)
    {
        super(context, 0, categories);

        for(int i=0;i<categories.length;i++)
        {
            this.categories.add(new CourseYearCategory(getContext()));
            this.categories.get(i).setCategory(categories[i]);
        }
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (categories.get(position) == null)
        {
            Integer category = getItem(position);

            categories.add(new CourseYearCategory(getContext()));
            categories.get(position).setCategory(category != null ? category : 1);
        }

        return categories.get(position);
    }
}
