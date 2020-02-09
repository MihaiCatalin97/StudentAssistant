const admin = require('firebase-admin');
const notifications = require('../notificationFunctions/functions');
const utils = require('../utils/functions');
const users = require('../userFunctions/functions');

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

exports.notifyStudentOnCourseFileAction = (courseKey, action) => {
    return getCourseStudents(courseKey)
        .then(students => {
            return users.getTokensForPersonUUIDs(students);
        })
        .then(tokens => {
            return notifications.sendNotifications(`File ${utils.getCompleteActionString(action).toLowerCase()} course`,
                `A file has been ${utils.getCompleteActionString(action).toLowerCase()} ${course.disciplineName}!`,
                tokens);
        });
}

exports.notifyStudentOnCourseContainmentAction = (courseKey, studentKey, action) => {
    const course = this.getById(courseKey);
    const token = users.getTokenForPersonUUID(studentKey);

    return Promise.all([course, token]).then(result => {
        const receivedCourse = result[0];
        const receivedToken = result[1];

        return notifications.sendNotification(`${utils.getCompleteActionString(action)} course`,
            `You have been ${utils.getCompleteActionString(action).toLowerCase()} ${receivedCourse.disciplineName}!`,
            receivedToken);
    });
}

exports.notifyStudentOnCourseEnrollmentAction = (courseKey, studentKey) => {
    const course = this.getById(courseKey);
    const token = users.getTokenForPersonUUID(studentKey);

    return Promise.all([course, token]).then(result => {
        const receivedCourse = result[0];
        const receivedToken = result[1];

        const enrollmentAction = receivedCourse.students.includes(studentKey) ? `accepted` : `declined`;

        return notifications.sendNotification(`Enrollment request ${enrollmentAction}`,
            `Your enrollment request for the course ${receivedCourse.disciplineName} has been ${enrollmentAction}!`,
            receivedToken);
    });
}

exports.getCourseStudents = courseKey => {
    return this.getById(courseKey).then(course => {
        return course.students;
    });
}