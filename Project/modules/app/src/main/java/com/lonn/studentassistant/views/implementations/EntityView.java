package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.AnnouncementLayoutViewBinding;
import com.lonn.studentassistant.databinding.EntityConstraintLayoutViewBinding;
import com.lonn.studentassistant.databinding.GradeCourseTableRowBinding;
import com.lonn.studentassistant.databinding.GradeLaboratoryTableRowBinding;
import com.lonn.studentassistant.databinding.OneTimeClassTableRowBinding;
import com.lonn.studentassistant.databinding.RecurringClassTableRowBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.viewModels.ScrollViewEntityViewModel;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import lombok.Getter;
import lombok.Setter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.lonn.studentassistant.BR.editing;
import static com.lonn.studentassistant.BR.unlinkable;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.NONE;
import static com.lonn.studentassistant.viewModels.ScrollViewEntityViewModelFactory.getScrollViewEntityViewModel;
import static com.lonn.studentassistant.views.EntityViewType.PARTIAL;

public class EntityView<T extends EntityViewModel> extends ScrollViewItem {
    protected EntityViewType viewType;
    @Getter
    protected T entityViewModel;
    @Getter
    protected ScrollViewEntityViewModel model;
    protected ViewDataBinding binding;
    protected PermissionLevel permissionLevel = NONE;
    @Setter
    protected Consumer<T> onDeleteTap;
    @Setter
    protected Consumer<T> onRemoveTap;
    @Setter
    protected Consumer<T> onApproveTap;

    public EntityView(Context context, EntityViewType viewType, PermissionLevel permissionLevel,
                      T entityViewModel) {
        super(context);

        this.viewType = viewType;
        this.permissionLevel = permissionLevel;
        this.entityViewModel = entityViewModel;

        init(context);
        updateEntity(entityViewModel);
    }

    public EntityView(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    public void inflateLayout(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);

            if (findViewById(R.id.entityRemoveButton) != null) {
                findViewById(R.id.entityRemoveButton).setOnClickListener((view) -> {
                    if (onRemoveTap != null) {
                        onRemoveTap.consume(entityViewModel);
                    }
                });
            }

            if (findViewById(R.id.entityDeleteButton) != null) {
                findViewById(R.id.entityDeleteButton).setOnClickListener((view) -> {
                    if (onDeleteTap != null) {
                        onDeleteTap.consume(entityViewModel);
                    }
                });
            }

            if (findViewById(R.id.entityApproveButton) != null) {
                findViewById(R.id.entityApproveButton).setOnClickListener((view) -> {
                    if (onApproveTap != null) {
                        onApproveTap.consume(entityViewModel);
                    }
                });
            }
        }
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
        model.permissionLevel = permissionLevel;
        setDataBindingVariable(model);
    }

    public void updateEntity(T newEntity) {
        entityViewModel = newEntity;

        model = getScrollViewEntityViewModel(viewType, entityViewModel);
        model.permissionLevel = permissionLevel;
        setDataBindingVariable(model);
    }

    public void setEditing(Boolean edit) {
        if (edit == null) {
            edit = false;
        }

        binding.setVariable(editing, edit);
    }

    public void setUnlinkable(Boolean unlink) {
        if (unlink == null) {
            unlink = false;
        }

        binding.setVariable(unlinkable, unlink);
    }

    public void setDeletable(Boolean delete) {
        if (delete == null) {
            delete = false;
        }

        binding.setVariable(com.lonn.studentassistant.BR.deletable, delete);
    }

    public void setCanApprove(Boolean canApprove) {
        if (canApprove == null) {
            canApprove = false;
        }

        binding.setVariable(com.lonn.studentassistant.BR.canApprove, canApprove);
    }

    protected int getLayoutId() {
        if (entityViewModel instanceof OneTimeClassViewModel) {
            return R.layout.one_time_class_table_row;
        }
        if (entityViewModel instanceof RecurringClassViewModel) {
            return R.layout.recurring_class_table_row;
        }
        if (entityViewModel instanceof GradeViewModel) {
            if (viewType.equals(PARTIAL)) {
                return R.layout.grade_laboratory_table_row;
            }
            return R.layout.grade_course_table_row;
        }
        if (entityViewModel instanceof AnnouncementViewModel) {
            return R.layout.announcement_layout_view;
        }

        return R.layout.entity_constraint_layout_view;
    }

    private void setDataBindingVariable(ScrollViewEntityViewModel model) {
        if (entityViewModel instanceof OneTimeClassViewModel) {
            ((OneTimeClassTableRowBinding) binding).setOneTimeClass((OneTimeClassViewModel) entityViewModel);
        }
        else if (entityViewModel instanceof RecurringClassViewModel) {
            ((RecurringClassTableRowBinding) binding).setRecurringClass((RecurringClassViewModel) entityViewModel);
        }
        else if (entityViewModel instanceof GradeViewModel) {
            if (viewType.equals(PARTIAL)) {
                ((GradeLaboratoryTableRowBinding) binding).setGrade((GradeViewModel) entityViewModel);
            }
            else {
                ((GradeCourseTableRowBinding) binding).setGrade((GradeViewModel) entityViewModel);
            }
        }
        else if (entityViewModel instanceof AnnouncementViewModel) {
            ((AnnouncementLayoutViewBinding) binding).setAnnouncement((AnnouncementViewModel) entityViewModel);
        }
        else if (onApproveTap != null) {
            ((EntityConstraintLayoutViewBinding) binding).setEntityViewModel(model);
        }
        else {
            ((EntityConstraintLayoutViewBinding) binding).setEntityViewModel(model);
        }
    }
}
