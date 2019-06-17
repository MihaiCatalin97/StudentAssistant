package com.lonn.studentassistant.views.dialogBuilders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Window;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.entities.Course;

import java.util.LinkedList;
import java.util.List;

public class OptionalCourseDialogBuilder extends AlertDialog.Builder
{
    private List<Course> optionalCourses;
    private List<Integer> checkedCourses = new LinkedList<>();

    public OptionalCourseDialogBuilder(Context context, final List<Course> courses)
    {
        super(context, R.style.DialogTheme);

        String[] courseTitles = new String[courses.size()];

        for(int i=0;i<courses.size();i++)
        {
            courseTitles[i] = courses.get(i).toString();
        }

        setTitle("Optional courses");
        setMultiChoiceItems(courseTitles,
                null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        if(isChecked)
                            checkedCourses.add(which);
                        else
                            checkedCourses.remove(Integer.valueOf(which));
                    }
                }
        );


        setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                List<Course> selectedCourses = new LinkedList<>();

                for(Integer position : checkedCourses)
                {
                    selectedCourses.add(courses.get(position));
                }

                StudentActivity.getInstance().enrollOptionalCourses(selectedCourses);
                Log.e("Enrolling courses 1", "YES");
            }
        });

        setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
    }

    public void showDialog()
    {
        final AlertDialog dialog = create();


        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0)
            {
                Window dialogWindow = dialog.getWindow();

                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                if(dialogWindow != null)
                {
                    dialogWindow.setBackgroundDrawableResource(R.drawable.dark_stroke_solid_background);
                }
            }
        });

        dialog.show();
    }
}
