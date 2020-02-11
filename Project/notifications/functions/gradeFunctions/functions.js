const users = require('../userFunctions/functions');
const courses = require('../courseFunctions/functions');

exports.notifyStudentOnGradeAction = (grade, action) => {
    return courses.getById(grade.courseKey)
        .then(course => {
            return users.sendNotificationToUUIDs(`Grade ${action}`,
                `Your ${formatGradeType(grade)} grade for ${course.disciplineName} has been ${action}!`,
                [grade.studentKey]);
        });
}

const gradeTypeToString = (gradeType) => {
    return gradeType.replace("_", " ").toLowerCase();
}

const formatGradeType = (grade) => {
    if (grade.gradeType === "LABORATORY") {
        return gradeTypeToString(grade.gradeType) + " " + (grade.laboratoryNumber ? grade.laboratoryNumber : 0);
    }
    return gradeTypeToString(grade.gradeType);
} 