const admin = require('firebase-admin');

exports.getByIds = studentKeys => {
    const studentPromises = [];

    for (let i = 0; i < studentKeys.length; i++) {
        studentPromises.push(admin.database().ref(`/dev/Students/${studentKeys[i]}`).once("value")
            .then(snapshot => {
                const snapshotVal = snapshot.val();

                return {
                    key: studentKeys[i],
                    student: snapshotVal
                };
            })
            .catch(error => { console.log(`Error reading student with id ${studentKeys[i]}: `, error) }));
    }

    return Promise.all(studentPromises);
}

exports.filterStudentsForScheduleGroup = (students, scheduleGroups) => {
    const resultStudents = [];

    for (let i = 0; i < students.length; i++) {
        if (this.studentBelongsToGroups(students[i].student, scheduleGroups)) {
            resultStudents.push(students[i]);
        }
    }

    return resultStudents;
}

exports.getStudentsForScheduleGroup = scheduleGroups => {
    return admin.database().ref(`/dev/Students`).once("value")
        .then(snapshot => {
            const students = snapshot.val();
            const resultStudents = [];
            const studentKeys = Object.keys(students);

            for (let i = 0; i < studentKeys.length; i++) {
                const studentKey = studentKeys[i];
                const student = students[studentKey];

                if (this.studentBelongsToGroups(student, scheduleGroups)) {
                    resultStudents.push({
                        key: studentKey,
                        student: student
                    });
                }
            }

            return resultStudents;
        })
        .catch(error => {
            console.log(`Error while reading students for group ` + scheduleGroup + `: `, error)
        });
}

exports.studentBelongsToGroups = (student, groups) => {
    if (student.group) {
        for (let i = 0; i < groups.length; i++) {
            if (student.group.startsWith(groups[i])) {
                return true;
            }
        }
    }
    if (groups.length === 0) {
        return true;
    }
    return false;
}