package com.lonn.studentassistant.firebaselayer.businessLayer.api;

import android.content.Context;

import com.lonn.studentassistant.firebaselayer.businessLayer.services.AnnouncementService;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.AdministratorService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.AuthenticationService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.CourseService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.EmailService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.FileContentService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.FileMetadataService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.GradeService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.LaboratoryService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.OneTimeClassService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.OtherActivityService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.ProfessorService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.RecurringClassService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.RegistrationTokenService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.RoomService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.StudentService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.UserService;

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
	@Getter
	private LaboratoryService laboratoryService;
	@Getter
	private GradeService gradeService;
	@Getter
	private RoomService roomService;
	@Getter
	private RegistrationTokenService registrationTokenService;
	@Getter
	private AnnouncementService announcementService;

	private FirebaseApi(Context applicationContext) {
		FirebaseConnection firebaseConnection = FirebaseConnection.getInstance(applicationContext);

		EmailService.getInstance();

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
		laboratoryService = LaboratoryService.getInstance(firebaseConnection);
		gradeService = GradeService.getInstance(firebaseConnection);
		roomService = RoomService.getInstance(firebaseConnection);
		registrationTokenService = RegistrationTokenService.getInstance(firebaseConnection);
		announcementService = AnnouncementService.getInstance(firebaseConnection);
	}

	public static FirebaseApi getApi(Context context) {
		if (instance == null) {
			instance = new FirebaseApi(context);
		}

		return instance;
	}
}
