package com.lonn.studentassistant.activities.implementations.student.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

import java.util.List;

public class ProfessorAdapter extends ArrayAdapter<Professor>
{
    public ProfessorAdapter(Context context, List<Professor> professors) {
        super(context, 0, professors);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Professor professor = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
        }

        if (professor != null) {
            TextView tvName = convertView.findViewById(R.id.textTitlu);
            tvName.setText(professor.firstName + " " + professor.lastName);
        }

        return convertView;
    }
}
