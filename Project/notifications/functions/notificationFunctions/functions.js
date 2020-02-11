const admin = require('firebase-admin');

exports.sendNotification = (title, message, deviceToken) => {
    const payload = buildPayload(title, message);

    if (deviceToken) {
        return admin.messaging().sendToDevice(deviceToken, payload)
            .then(response => {
                console.log("Successfully sent message:", response);
                return response;
            })
            .catch(error => {
                console.log("Error sending message:", error);
            });
    }
    return {
        error: `Invalid token "${deviceToken}"`
    }
}

exports.sendNotifications = (title, message, deviceTokens) => {
    const payload = buildPayload(title, message);
    const notificationPromises = [];
    deviceTokens = deviceTokens.filter((a, b) => deviceTokens.indexOf(a) === b);

    for (let i = 0; i < deviceTokens.length; i++) {
        if (deviceTokens[i]) {
            notificationPromises.push(admin.messaging().sendToDevice(deviceTokens[i], payload));
        }
    }

    return Promise.all(notificationPromises)
        .then(responses => {
            for (let i = 0; i < responses.length; i++) {
                console.log("Successfully sent notification:", responses[i]);
            }
            return responses;
        })
        .catch(error => {
            console.log("Error sending notification:", error);
        });
}

const buildPayload = (title, message) => {
    return {
        notification: {
            title: title,
            body: message
        }
    };
}