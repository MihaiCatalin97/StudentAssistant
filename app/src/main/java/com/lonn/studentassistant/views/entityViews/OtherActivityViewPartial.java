package com.lonn.studentassistant.views.entityViews;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.databinding.OtherActivityViewPartialBinding;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.views.abstractions.EntityView;

public class OtherActivityViewPartial extends EntityView<OtherActivity>
{
    OtherActivityViewPartialBinding binding;

    public OtherActivityViewPartial(Context context)
    {
        super(context);
    }

    public OtherActivityViewPartial(Context context, OtherActivity otherActivity)
    {
        super(context);
        entity = otherActivity;
        model = new OtherActivityViewModel(entity);
        binding.setActivity((OtherActivityViewModel) model);
    }

    public void inflateLayout(final Context context)
    {
        binding = DataBindingUtil.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.other_activity_view_partial, this, true);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent courseIntent = new Intent(context, CourseEntityActivity.class);

                courseIntent.putExtra("course", entity);

                context.startActivity(courseIntent);
            }
        });
    }

    public void update(OtherActivity newActivity)
    {
        entity = newActivity;

        if (model == null)
        {
            model = new OtherActivityViewModel(newActivity);
            binding.setActivity((OtherActivityViewModel)model);
        }
        else
        {
            model.update(newActivity);
        }

        model.notifyPropertyChanged(BR._all);
    }
}