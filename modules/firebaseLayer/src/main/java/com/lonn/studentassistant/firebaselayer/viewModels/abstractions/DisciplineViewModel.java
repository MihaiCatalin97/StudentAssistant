package com.lonn.studentassistant.firebaselayer.viewModels.abstractions;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static com.lonn.studentassistant.firebaselayer.Utils.semesterToString;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public abstract class DisciplineViewModel<T extends Discipline> extends FileAssociatedEntityViewModel<T> {
    @Bindable
    protected String name, description, website;

    protected int semester;
    protected List<String> pendingStudents = new LinkedList<>();
    protected List<String> students = new LinkedList<>();
    protected List<String> professors = new ArrayList<>();
    protected List<String> recurringClasses = new ArrayList<>();
    protected List<String> oneTimeClasses = new ArrayList<>();

    @Bindable
    public String getSemesterString() {
        return semesterToString(semester);
    }

    @Bindable
    public int getWebsiteVisible() {
        return (website != null) ? View.VISIBLE : View.GONE;
    }

    @Override
    public DisciplineViewModel<T> setKey(String key) {
        super.setKey(key);
        return this;
    }

    public DisciplineViewModel<T> setFileMetadataKeys(List<String> filesMetadata) {
        this.fileMetadataKeys = filesMetadata;
        return this;
    }
}
