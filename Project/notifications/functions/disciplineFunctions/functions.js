const utils = require('../utils/functions');
const users = require('../userFunctions/functions');

exports.notifyStudentOnFileAction = (discipline, action, disciplineType) => {
    if (discipline) {
        return users.sendNotificationToUUIDs(`File ${utils.getCompleteActionString(action).toLowerCase()} ${disciplineType}`,
            `A file has been ${utils.getCompleteActionString(action).toLowerCase()} ${discipline.disciplineName}!`,
            discipline.students);
    }
    return null;
}

exports.notifyPersonOnContainmentAction = (discipline, personKey, action, disciplineType) => {
    if (discipline) {
        return users.sendNotificationToUUIDs(`${utils.getCompleteActionString(action)} ${disciplineType}`,
            `You have been ${utils.getCompleteActionString(action).toLowerCase()} ${discipline.disciplineName}!`,
            [personKey]);
    }
    return null;
}

exports.notifyProfessorsOnEnrollmentAdded = (discipline, disciplineType) => {
    if (discipline) {
        return users.sendNotificationToUUIDs(`New enrollment request`,
            `A new enrollment request for the ${disciplineType} ${discipline.disciplineName} has been added!`,
            Object.values(discipline.professors));
    }
    return null;
}

exports.notifyStudentOnEnrollmentAction = (discipline, studentKey, disciplineType) => {
    if (discipline) {
        const enrollmentAction = discipline.students.includes(studentKey) ? `accepted` : `declined`;

        return users.sendNotificationToUUIDs(`Enrollment request ${enrollmentAction}`,
            `Your enrollment request for the ${disciplineType} ${discipline.disciplineName} has been ${enrollmentAction}!`,
            [studentKey]);
    }
    return null;
}