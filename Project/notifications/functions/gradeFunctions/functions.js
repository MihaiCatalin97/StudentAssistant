const users = require('../userFunctions/functions');
const courses = require('../courseFunctions/functions');
const notifications = require('../notificationFunctions/functions');

exports.notifyStudentOnGradeAction = (courseKey, studentKey, gradeType, action) => {
    const course = courses.getById(courseKey);
    const token = users.getTokenForPersonUUID(studentKey);

    return Promise.all([course, token])
        .then(result => {
            const course = result[0];
            const token = result[1];

            return notifications.sendNotification(`Grade ${action}`,
                `Your ${gradeTypeToString(gradeType)} grade for ${course.disciplineName} has been ${action}!`,
                token);
        })
        .catch(error => { console.log(error) });
}

const gradeTypeToString = (gradeType) => {
    return gradeType.replace("_", " ").toLowerCase();
} 