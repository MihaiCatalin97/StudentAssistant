const functions = require('firebase-functions');
const admin = require('firebase-admin');
const email = require('./emailFunctions/functions');
const courses = require('./courseFunctions/functions');
const grades = require('./gradeFunctions/functions');
const laboratories = require('./laboratoryFunctions/functions');
const schedule = require('./scheduleFunctions/functions');
const activities = require('./activityFunctions/functions');
const notifications = require('./notificationFunctions/functions');
const users = require('./userFunctions/functions');
admin.initializeApp();

exports.sendMail = functions.https.onCall((data, context) => {
    if (context.auth) {
        return email.sendInvitationEmail(data.dest, data.accountType, data.token, data.expiresAt);
    }
    return {
        result: "error",
        error: "Not logged in"
    }
});

exports.sendGradeAddedNotification =
    functions.database.ref('/dev/Grades/{uid}').onCreate((change, context) => {
        return grades.notifyStudentOnGradeAction(
            change._data,
            "added");
    });

exports.sendGradeDeletedNotification =
    functions.database.ref('/dev/Grades/{uid}').onDelete((change, context) => {
        return grades.notifyStudentOnGradeAction(
            change._data,
            "deleted");
    });

exports.sendStudentAddedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/students/{uid2}').onCreate((change, context) => {
        return courses.notifyPersonOnContainmentAction(context.params.uid1, change._data, "added");
    });

exports.sendStudentRemovedFromCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/students/{uid2}').onDelete((change, context) => {
        return courses.notifyPersonOnContainmentAction(context.params.uid1, change._data, "removed");
    });

exports.sendStudentAddedToActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/students/{uid2}').onCreate((change, context) => {
        return activities.notifyPersonOnContainmentAction(context.params.uid1, change._data, "added");
    });

exports.sendStudentRemovedFromActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/students/{uid2}').onDelete((change, context) => {
        return activities.notifyPersonOnContainmentAction(context.params.uid1, change._data, "removed");
    });

exports.sendProfessorAddedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/professors/{uid2}').onCreate((change, context) => {
        return courses.notifyPersonOnContainmentAction(context.params.uid1, change._data, "added");
    });

exports.sendProfessorRemovedFromCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/professors/{uid2}').onDelete((change, context) => {
        return courses.notifyPersonOnContainmentAction(context.params.uid1, change._data, "removed");
    });

exports.sendProfessorAddedToActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/professors/{uid2}').onCreate((change, context) => {
        return activities.notifyPersonOnContainmentAction(context.params.uid1, change._data, "added");
    });

exports.sendProfessorRemovedFromActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/professors/{uid2}').onDelete((change, context) => {
        return activities.notifyPersonOnContainmentAction(context.params.uid1, change._data, "removed");
    });

exports.sendEnrollmentAddedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/pendingStudents/{uid2}').onCreate((change, context) => {
        return courses.notifyProfessorsOnEnrollmentAdded(context.params.uid1);
    });

exports.sendEnrollmentDeletedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/pendingStudents/{uid2}').onDelete((change, context) => {
        return courses.notifyStudentOnEnrollmentAction(context.params.uid1, change._data);
    });

exports.sendEnrollmentAddedToActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/pendingStudents/{uid2}').onCreate((change, context) => {
        return activities.notifyProfessorsOnEnrollmentAdded(context.params.uid1);
    });

exports.sendEnrollmentDeletedToActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/pendingStudents/{uid2}').onDelete((change, context) => {
        return activities.notifyStudentOnEnrollmentAction(context.params.uid1, change._data);
    });

exports.sendFileAddedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/fileMetadataKeys/{uid2}').onCreate((change, context) => {
        return courses.notifyStudentOnFileAction(context.params.uid1, "added");
    });

exports.sendFileRemovedFromCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/fileMetadataKeys/{uid2}').onDelete((change, context) => {
        return courses.notifyStudentOnFileAction(context.params.uid1, "removed");
    });

exports.sendFileAddedToActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/fileMetadataKeys/{uid2}').onCreate((change, context) => {
        return activities.notifyStudentOnFileAction(context.params.uid1, "added");
    });

exports.sendFileRemovedFromActivityNotification =
    functions.database.ref('/dev/Otheractivities/{uid1}/fileMetadataKeys/{uid2}').onDelete((change, context) => {
        return activities.notifyStudentOnFileAction(context.params.uid1, "removed");
    });

exports.sendLaboratoryAddedNotification =
    functions.database.ref('/dev/Laboratories/{uid}').onCreate((change, context) => {
        return laboratories.notifyStudentOnLaboratoryAction(change._data, "added");
    });

exports.sendLaboratoryRemovedNotification =
    functions.database.ref('/dev/Laboratories/{uid}').onDelete((change, context) => {
        return laboratories.notifyStudentOnLaboratoryAction(change._data, "removed");
    });

exports.sendLaboratoryFileAddedNotification =
    functions.database.ref('/dev/Laboratories/{uid1}/fileMetadataKeys/{uid2}').onCreate((change, context) => {
        return laboratories.notifyStudentOnLaboratoryFileAction(context.params.uid1, "added");
    });

exports.sendLaboratoryFileRemovedNotification =
    functions.database.ref('/dev/Laboratories/{uid1}/fileMetadataKeys/{uid2}').onDelete((change, context) => {
        return laboratories.notifyStudentOnLaboratoryFileAction(context.params.uid1, "removed");
    });

exports.sendRegularScheduleAddedNotification =
    functions.database.ref('/dev/Schedule/recurring/{uid}').onCreate((change, context) => {
        return schedule.notifyPersonOnScheduleAction(change._data, "added", "Regular");
    });

exports.sendRegularScheduleRemovedNotification =
    functions.database.ref('/dev/Schedule/recurring/{uid}').onDelete((change, context) => {
        return schedule.notifyPersonOnScheduleAction(change._data, "removed", "Regular");
    });

exports.sendSpecialScheduleAddedNotification =
    functions.database.ref('/dev/Schedule/onetime/{uid}').onCreate((change, context) => {
        return schedule.notifyPersonOnScheduleAction(change._data, "added", "Special");
    });

exports.sendSpecialScheduleRemovedNotification =
    functions.database.ref('/dev/Schedule/onetime/{uid}').onDelete((change, context) => {
        return schedule.notifyPersonOnScheduleAction(change._data, "removed", "Special");
    });

exports.sendCourseAddedNotification =
    functions.database.ref('/dev/Courses/{uid}').onCreate((change, context) => {
        console.log(change._data);
        return users.getAllTokens()
            .then(tokens => {
                return notifications.sendNotifications("Course added to Student Assistant",
                    `The course ${change._data.disciplineName} has been added to the application.`,
                    tokens);
            });
    });

exports.sendCourseDeletedNotification =
    functions.database.ref('/dev/Courses/{uid}').onDelete((change, context) => {
        console.log(change._data);
        return users.getAllTokens()
            .then(tokens => {
                return notifications.sendNotifications("Course deleted from Student Assistant",
                    `The course ${change._data.disciplineName} has been deleted from the application.`,
                    tokens);
            });
    });

exports.sendActivityAddedNotification =
    functions.database.ref('/dev/Otheractivities/{uid}').onCreate((change, context) => {
        console.log(change._data);
        return users.getAllTokens()
            .then(tokens => {
                return notifications.sendNotifications("Activity added to Student Assistant",
                    `The activity ${change._data.disciplineName} has been added to the application.`,
                    tokens);
            });
    });

exports.sendActivityDeletedNotification =
    functions.database.ref('/dev/Otheractivities/{uid}').onDelete((change, context) => {
        console.log(change._data);
        return users.getAllTokens()
            .then(tokens => {
                return notifications.sendNotifications("Activity deleted from Student Assistant",
                    `The activity ${change._data.disciplineName} has been deleted from the application.`,
                    tokens);
            });
    });