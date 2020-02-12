const courses = require('../courseFunctions/functions');
const activities = require('../activityFunctions/functions');
const students = require('../studentFunctions/functions');
const users = require('../userFunctions/functions');
const utils = require('../utils/functions');
const admin = require('firebase-admin');

exports.getRegularClassById = regularClassKey => {
    return admin.database().ref(`/dev/Schedule/recurring/${regularClassKey}`).once("value")
        .then(snapshot => {
            return {
                key: Object.keys(snapshot.val())[0],
                scheduleClass: Object.values(snapshot.val())[0]
            };
        })
        .catch(error => {
            console.log(`Error while reading regular class with key ` + regularClassKey + `: `, error)
        });
}

exports.getOneTimeClassById = oneTimeClassKey => {
    return admin.database().ref(`/dev/Schedule/onetime/${oneTimeClassKey}`).once("value")
        .then(snapshot => {
            return {
                key: Object.keys(snapshot.val())[0],
                scheduleClass: Object.values(snapshot.val())[0]
            };
        })
        .catch(error => {
            console.log(`Error while reading one time class with key ` + oneTimeClassKey + `: `, error)
        });
}

exports.notifyPersonOnScheduleAction = (scheduleClass, action, scheduleClassType) => {
    if (scheduleClass.groups) {
        groups = Object.values(scheduleClass.groups);
    }
    else {
        groups = [];
    }

    return courses.getById(scheduleClass.discipline)
        .then(course => {
            return sendScheduleActionNotification(course, groups, action, `course`, scheduleClassType);
        })
        .then(courseResult => {
            if (!courseResult) {
                return activities.getById(scheduleClass.discipline);
            }
            return null;
        })
        .then(activity => {
            return sendScheduleActionNotification(activity, groups, action, `activity`, scheduleClassType);
        });
}

const sendScheduleActionNotification = (discipline, groups, action, disciplineType, scheduleClassType) => {
    if (discipline) {
        if (discipline.students) {
            return sendScheduleActionNotificationToGroups(discipline, groups, action, disciplineType, scheduleClassType)
                .then(promise => {
                    return sendScheduleActionNotificationToProfessors(discipline, action, disciplineType, scheduleClassType);
                });
        }
        return sendScheduleActionNotificationToProfessors(discipline, action, disciplineType, scheduleClassType);
    }
    return null;
}

const sendScheduleActionNotificationToGroups = (discipline, groups, action, disciplineType, scheduleType) => {
    return students.getByIds(discipline.students)
        .then(disciplineStudents => {
            return students.filterStudentsForScheduleGroup(disciplineStudents, groups);
        })
        .then(filteredStudents => {
            const filteredStudentKeys = [];

            for (let i = 0; i < filteredStudents.length; i++) {
                filteredStudentKeys.push(filteredStudents[i].key);
            }

            return users.sendNotificationToUUIDs(`${scheduleType} schedule updated`,
                `A ${scheduleType.toLowerCase()} schedule class has been ${utils.getCompleteActionString(action).toLowerCase()} the ${disciplineType} ${discipline.disciplineName}!`,
                filteredStudentKeys);
        });
};

const sendScheduleActionNotificationToProfessors = (discipline, action, disciplineType, scheduleType) => {
    return users.sendNotificationToUUIDs(`${scheduleType} schedule updated`,
        `A ${scheduleType.toLowerCase()} schedule class has been ${utils.getCompleteActionString(action).toLowerCase()} the ${disciplineType} ${discipline.disciplineName}!`,
        Object.values(discipline.professors));
};