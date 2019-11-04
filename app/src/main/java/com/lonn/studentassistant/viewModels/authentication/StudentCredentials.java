package com.lonn.studentassistant.viewModels.authentication;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.models.Student;

import lombok.ToString;

@ToString
public class StudentCredentials extends BaseObservable {
    /*TODO: Investigate transport method for hashing inside the firebaseLayer
     * */
    @Bindable
    public String studentId;
    @Bindable
    public String lastName;
    @Bindable
    public String firstName;
    @Bindable
    public String fatherInitial;
    @Bindable
    public String phoneNumber;

    public Student toStudent() {
        return new Student()
                .setFatherInitial(fatherInitial)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPhoneNumber(phoneNumber)
                .setStudentId(studentId);
    }
}
