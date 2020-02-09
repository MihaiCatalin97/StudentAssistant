const notifications = require('../notificationFunctions/functions');
const utils = require('../utils/functions');
const users = require('../userFunctions/functions');
const courses = require('../courseFunctions/functions');

exports.notifyStudentOnLaboratoryAction = (laboratory, action) => {
    return courses.getById(laboratory.course)
        .then(course => {
            return sendLaboratoryActionNotification(course.disciplineName,
                laboratory,
                action,
                users.getTokensForPersonUUIDs(course.students));
        });
}

const sendLaboratoryActionNotification = (courseName, laboratory, action, userTokensPromise) => {
    return userTokensPromise.then(tokens => {
        return notifications.sendNotifications(`Laboratory ${utils.getCompleteActionString(action).toLowerCase()} course`,
            `${laboratory.title} (Week ${laboratory.weekNumber}) has been ${utils.getCompleteActionString(action).toLowerCase()} ${courseName}!`,
            tokens);
    });
}