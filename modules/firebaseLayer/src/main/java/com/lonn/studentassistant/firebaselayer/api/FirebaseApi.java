package com.lonn.studentassistant.firebaselayer.api;

import android.content.Context;

import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.AdministratorService;
import com.lonn.studentassistant.firebaselayer.services.AuthenticationService;
import com.lonn.studentassistant.firebaselayer.services.CourseService;
import com.lonn.studentassistant.firebaselayer.services.FileContentService;
import com.lonn.studentassistant.firebaselayer.services.FileMetadataService;
import com.lonn.studentassistant.firebaselayer.services.OneTimeClassService;
import com.lonn.studentassistant.firebaselayer.services.OtherActivityService;
import com.lonn.studentassistant.firebaselayer.services.ProfessorService;
import com.lonn.studentassistant.firebaselayer.services.RecurringClassService;
import com.lonn.studentassistant.firebaselayer.services.StudentService;
import com.lonn.studentassistant.firebaselayer.services.UserService;

import lombok.Getter;

public class FirebaseApi {
	private static FirebaseApi instance;

	@Getter
	private AdministratorService administratorService;
	@Getter
	private CourseService courseService;
	@Getter
	private OtherActivityService otherActivityService;
	@Getter
	private AuthenticationService authenticationService;
	@Getter
	private FileContentService fileContentService;
	@Getter
	private FileMetadataService fileMetadataService;
	@Getter
	private OneTimeClassService oneTimeClassService;
	@Getter
	private ProfessorService professorService;
	@Getter
	private RecurringClassService recurringClassService;
	@Getter
	private StudentService studentService;
	@Getter
	private UserService userService;

	private FirebaseApi(Context applicationContext) {
		FirebaseConnection firebaseConnection = FirebaseConnection.getInstance(applicationContext);

		administratorService = AdministratorService.getInstance(firebaseConnection);
		courseService = CourseService.getInstance(firebaseConnection);
		otherActivityService = OtherActivityService.getInstance(firebaseConnection);
		authenticationService = AuthenticationService.getInstance(firebaseConnection);
		fileContentService = FileContentService.getInstance(firebaseConnection);
		fileMetadataService = FileMetadataService.getInstance(firebaseConnection);
		oneTimeClassService = OneTimeClassService.getInstance(firebaseConnection);
		professorService = ProfessorService.getInstance(firebaseConnection);
		recurringClassService = RecurringClassService.getInstance(firebaseConnection);
		studentService = StudentService.getInstance(firebaseConnection);
		userService = UserService.getInstance(firebaseConnection);
	}

	public static FirebaseApi getApi(Context context) {
		if (instance == null) {
			instance = new FirebaseApi(context);
		}

		return instance;
	}
}
