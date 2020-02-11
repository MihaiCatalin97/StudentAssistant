const admin = require('firebase-admin');
const disciplines = require('../disciplineFunctions/functions');

exports.getById = courseKey => {
    return admin.database().ref(`/dev/Courses/${courseKey}`).once("value")
        .then(snapshot => {
            course = snapshot.val();
            return course;
        })
        .catch(error => {
            console.log(`Error while reading course with key ` + courseKey + `: `, error)
        });
}

exports.notifyStudentOnFileAction = (courseKey, action) => {
    return this.getById(courseKey)
        .then(course => {
            return disciplines.notifyStudentOnFileAction(course, action, `course`);
        });
}

exports.notifyPersonOnContainmentAction = (courseKey, personKey, action) => {
    return this.getById(courseKey)
        .then(course => {
            return disciplines.notifyPersonOnContainmentAction(course, personKey, action, `course`);
        });
}

exports.notifyProfessorsOnEnrollmentAdded = (courseKey) => {
    return this.getById(courseKey)
        .then(course => {
            return disciplines.notifyProfessorsOnEnrollmentAdded(course, `course`);
        });
}

exports.notifyStudentOnEnrollmentAction = (courseKey, studentKey) => {
    return this.getById(courseKey)
        .then(course => {
            return disciplines.notifyStudentOnEnrollmentAction(course, studentKey, `course`);
        });
}

exports.getStudents = courseKey => {
    return this.getById(courseKey).then(course => {
        return course.students;
    });
}