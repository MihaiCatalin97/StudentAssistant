const utils = require('../utils/functions');
const users = require('../userFunctions/functions');
const courses = require('../courseFunctions/functions');
const admin = require('firebase-admin');

exports.getById = laboratoryKey => {
    return admin.database().ref(`/dev/Laboratories/${laboratoryKey}`).once("value")
        .then(snapshot => {
            return snapshot.val();
        })
        .catch(error => {
            console.log(`Error while reading laboratory with key ` + courseKey + `: `, error)
        });
}

exports.notifyStudentOnLaboratoryAction = (laboratory, action) => {
    return courses.getById(laboratory.course)
        .then(course => {
            return users.sendNotificationToUUIDs(`Laboratory ${utils.getCompleteActionString(action).toLowerCase()} course`,
                `${laboratory.title} (Week ${laboratory.weekNumber}) has been ${utils.getCompleteActionString(action).toLowerCase()} ${course.disciplineName}!`,
                course.students);
        });
}

exports.notifyStudentOnLaboratoryFileAction = (laboratoryKey, action) => {
    return this.getById(laboratoryKey)
        .then(laboratory => {
            return sendLaboratoryFileActionNotification(laboratory, action);
        })
}

const sendLaboratoryFileActionNotification = (laboratory, action) => {
    return courses.getById(laboratory.course).then(course => {
        return users.sendNotificationToUUIDs(`File ${utils.getCompleteActionString(action).toLowerCase()} laboratory`,
            `A file has been ${utils.getCompleteActionString(action).toLowerCase()} the laboratory "${laboratory.title}" (Week ${laboratory.weekNumber}) of ${course.disciplineName}!`,
            course.students);
    });
}