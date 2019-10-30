package com.lonn.studentassistant.views.implementations.dialogBuilders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.LinkedList;
import java.util.List;

public class DialogBuilder extends AlertDialog.Builder
{
    private List<Integer> checkedItems = new LinkedList<>();

    public DialogBuilder(Context context, final List<BaseEntity> entities, String title, String positiveButtonText)
    {
        super(context, R.style.DialogTheme);

        String[] titles = new String[entities.size()];

        for(int i=0;i<entities.size();i++)
        {
            titles[i] = entities.get(i).toString();
        }

        setTitle(title);
        setMultiChoiceItems(titles,
                null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        if(isChecked)
                            checkedItems.add(which);
                        else
                            checkedItems.remove(Integer.valueOf(which));
                    }
                }
        );

        setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                for(Integer position : checkedItems)
                {
                    ServiceBoundActivity.getCurrentActivity().getBusinessLayer().addEntityToList(entities.get(position));
                }

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
