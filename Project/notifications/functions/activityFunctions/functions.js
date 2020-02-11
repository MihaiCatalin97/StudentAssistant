const admin = require('firebase-admin');
const disciplines = require('../disciplineFunctions/functions');

exports.getById = activityKey => {
    return admin.database().ref(`/dev/Otheractivities/${activityKey}`).once("value")
        .then(snapshot => {
            activity = snapshot.val();
            return activity;
        })
        .catch(error => {
            console.log(`Error while reading activity with key ` + activityKey + `: `, error)
        });
}

exports.notifyStudentOnFileAction = (activityKey, action) => {
    return this.getById(activityKey)
        .then(activity => {
            return disciplines.notifyStudentOnFileAction(activity, action, `activity`);
        });
}

exports.notifyProfessorsOnEnrollmentAdded = (activityKey) => {
    return this.getById(activityKey)
        .then(activity => {
            return disciplines.notifyProfessorsOnEnrollmentAdded(activity, `activity`);
        });
}

exports.notifyPersonOnContainmentAction = (activityKey, personKey, action) => {
    return this.getById(activityKey)
        .then(activity => {
            return disciplines.notifyPersonOnContainmentAction(activity, personKey, action, `activity`);
        });
}

exports.notifyStudentOnEnrollmentAction = (activityKey, studentKey) => {
    return this.getById(activityKey)
        .then(activity => {
            return disciplines.notifyStudentOnEnrollmentAction(activity, studentKey, `activity`);
        });
}

exports.getStudents = activityKey => {
    return this.getById(activityKey).then(activity => {
        return activity.students;
    });
}