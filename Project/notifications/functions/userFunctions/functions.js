const notifications = require('../notificationFunctions/functions');
const admin = require('firebase-admin');

exports.getUserByPersonUUID = personUUID => {
    return admin.database().ref(`/dev/Users/`)
        .orderByChild(`personUUID`)
        .equalTo(personUUID).once("value")
        .then(snapshot => {
            user = snapshot.val();

            if (user) {
                return user[Object.keys(user)[0]];
            }
            console.log(`No account linked to personUUID ${personUUID}`);
            return null;
        })
        .catch(error => {
            console.log(`Error while reading user with personUUID ` + personUUID + `: `, error)
        });
}

exports.getTokensForAccountTypes = accountTypes => {
    resultTokens = [];

    if (accountTypes) {
        return this.getAllStudentTokens()
            .then(tokens => {
                if (accountTypes.includes(`STUDENT`)) {
                    resultTokens = resultTokens.concat(tokens);
                }
                return this.getAllProfessorTokens();
            })
            .then(tokens => {
                if (accountTypes.includes(`PROFESSOR`)) {
                    resultTokens = resultTokens.concat(tokens);
                }
                return this.getAllAdministratorTokens();
            })
            .then(tokens => {
                if (accountTypes.includes(`ADMINISTRATOR`)) {
                    resultTokens = resultTokens.concat(tokens);
                }
                return resultTokens;
            });
    }

    return resultTokens;
}

exports.getAllStudentTokens = () => {
    return admin.database().ref(`/dev/Users/`)
        .orderByChild(`accountType`)
        .equalTo(`STUDENT`).once("value")
        .then(snapshot => {
            snapVal = snapshot.val();
            userKeys = Object.keys(snapVal);
            tokens = [];

            for (let i = 0; i < userKeys.length; i++) {
                token = snapVal[userKeys[i]].fcmToken;

                if (token) {
                    tokens.push(token);
                }
            }

            return tokens;
        })
        .catch(error => {
            console.log(`Error while reading all student tokens: `, error)
        });
}

exports.getAllProfessorTokens = () => {
    return admin.database().ref(`/dev/Users/`)
        .orderByChild(`accountType`)
        .equalTo(`PROFESSOR`).once("value")
        .then(snapshot => {
            snapVal = snapshot.val();
            userKeys = Object.keys(snapVal);
            tokens = [];

            for (let i = 0; i < userKeys.length; i++) {
                token = snapVal[userKeys[i]].fcmToken;

                if (token) {
                    tokens.push(token);
                }
            }

            return tokens;
        })
        .catch(error => {
            console.log(`Error while reading all professor tokens: `, error)
        });
}

exports.getAllAdministratorTokens = () => {
    return admin.database().ref(`/dev/Users/`)
        .orderByChild(`accountType`)
        .equalTo(`ADMINISTRATOR`).once("value")
        .then(snapshot => {
            snapVal = snapshot.val();
            userKeys = Object.keys(snapVal);
            tokens = [];

            for (let i = 0; i < userKeys.length; i++) {
                token = snapVal[userKeys[i]].fcmToken;

                if (token) {
                    tokens.push(token);
                }
            }

            return tokens;
        })
        .catch(error => {
            console.log(`Error while reading all user tokens: `, error)
        });
}

exports.getTokenForPersonUUID = personUUID => {
    return this.getUserByPersonUUID(personUUID)
        .then(user => {
            if (user) {
                return user.fcmToken;
            }
            return null;
        });
}

exports.getAllTokens = () => {
    return admin.database().ref(`/dev/Users/`).once("value")
        .then(snapshot => {
            users = Object.values(snapshot.val());
            const tokens = [];

            for (let i = 0; i < users.length; i++) {
                const token = users[i].fcmToken;

                if (token) {
                    tokens.push(token);
                }
            }

            return tokens;
        })
        .catch(error => {
            console.log(`Error while reading all user tokens: `, error)
        });
}

exports.getTokensForPersonUUIDs = personUUIDs => {
    const tokenPromises = [];

    for (let i = 0; i < personUUIDs.length; i++) {
        tokenPromises.push(this.getUserByPersonUUID(personUUIDs[i])
            .then(user => {
                if (user) {
                    return user.fcmToken;
                }
                return null;
            }));
    }

    return Promise.all(tokenPromises);
}

exports.sendNotificationToUUIDs = (title, message, personUUIDs) => {
    return this.getTokensForPersonUUIDs(personUUIDs)
        .then(tokens => {
            return notifications.sendNotifications(title, message, tokens);
        });
}