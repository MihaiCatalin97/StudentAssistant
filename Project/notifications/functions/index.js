const functions = require('firebase-functions');
const admin = require('firebase-admin');
const email = require('./emailFunctions/functions');
const courses = require('./courseFunctions/functions');
const grades = require('./gradeFunctions/functions');
const laboratories = require('./laboratoryFunctions/functions');
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
            change._data.courseKey,
            change._data.studentKey,
            change._data.gradeType,
            "added");
    });

exports.sendGradeDeletedNotification =
    functions.database.ref('/dev/Grades/{uid}').onDelete((change, context) => {
        return grades.notifyStudentOnGradeAction(
            change._data.courseKey,
            change._data.studentKey,
            change._data.gradeType,
            "deleted");
    });

exports.sendAddedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/students/{uid2}').onCreate((change, context) => {
        return courses.notifyStudentOnCourseContainmentAction(context.params.uid1, change._data, "added");
    });

exports.sendRemovedFromCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/students/{uid2}').onDelete((change, context) => {
        return courses.notifyStudentOnCourseContainmentAction(context.params.uid1, change._data, "removed");
    });

exports.sendEnrollmentToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/pendingStudents/{uid2}').onDelete((change, context) => {
        return courses.notifyStudentOnCourseEnrollmentAction(context.params.uid1, change._data);
    });

exports.sendFileAddedToCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/fileMetadataKeys/{uid2}').onCreate((change, context) => {
        return courses.notifyStudentOnCourseFileAction(context.params.uid1, "added");
    });

exports.sendFileRemovedFromCourseNotification =
    functions.database.ref('/dev/Courses/{uid1}/fileMetadataKeys/{uid2}').onDelete((change, context) => {
        return courses.notifyStudentOnCourseFileAction(context.params.uid1, "removed");
    });
    
exports.sendLaboratoryAddedNotification =
functions.database.ref('/dev/Laboratories/{uid}').onCreate((change, context) => {
    return laboratories.notifyStudentOnLaboratoryAction(change._data, "added");
});

exports.sendLaboratoryRemovedNotification =
functions.database.ref('/dev/Laboratories/{uid}').onDelete((change, context) => {
    return laboratories.notifyStudentOnLaboratoryAction(change._data, "removed");
});
