package com.lonn.studentassistant.views.entityViews;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.databinding.ProfessorViewBinding;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.abstractions.EntityView;

public class ProfessorView extends EntityView<Professor>
{
    ProfessorViewBinding binding;

    private ProfessorViewModel model;
    private Professor entity;

    public ProfessorView(Context context)
    {
        super(context);
    }

    public ProfessorView(Context context, Professor professor)
    {
        super(context);
        entity = professor;
        model = new ProfessorViewModel(entity);
        binding.setProfessor(model);
    }

    public void inflateLayout(final Context context)
    {
        binding = DataBindingUtil.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.professor_view, this, true);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent professorIntent = new Intent(context, ProfessorEntityActivity.class);

                professorIntent.putExtra("professor", entity);

                context.startActivity(professorIntent);
            }
        });
    }

    public void update(Professor newProfessor)
    {
        Log.e("Updating Professor", newProfessor.lastName);
        entity = newProfessor;

        if (model == null)
        {
            model = new ProfessorViewModel(newProfessor);
            binding.setProfessor(model);
            Log.e("Creating professor", model.lastName);
        }
        else
        {
            model.update(newProfessor);
            Log.e("Updating professor", model.lastName);
        }

        model.notifyPropertyChanged(BR._all);
    }
}
