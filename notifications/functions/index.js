const functions = require('firebase-functions');
const config = {
    email: 'student.assistant00@gmail.com',
    password: 'Studentassistant12'
}

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const admin = require('firebase-admin');
const nodemailer = require('nodemailer');
const cors = require('cors')({origin: true});
admin.initializeApp();

let transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: config.email,
        pass: config.password
    }
});

exports.sendMail = functions.https.onCall((data, context) => {
    if(context.auth){
        const dest = data.dest;
        const accountType = data.accountType;
        const token = data.token;
        const expiresAt = data.expiresAt;
    
        const mailOptions = {
            from: 'Student Assistant <student.assistant00@gmail.com>',
            to: dest,
            subject: 'Student Assistant - Invitation',
            html: `<p style="font-size: 16px;">Hello, ` + accountType + `</p>
                <br />
                <p>You have been invited to create an ` + accountType + ` account in the Student Assistant application.
                To register, please use this token: ` + token + `
                Beware, this is a confidential, single-use token and it will expire at ` + expiresAt + 
                `Download the app from:\n
                (app store link)</p>
            `
        };

        return transporter.sendMail(mailOptions)
        .catch((error) => {
            console.log("An error occurred while sending the registration token(" + accountType + ", " + token + ") to " + dest + ":\n" + error);

            return {
                result: "error", 
                error : error
            }
        })
        .then(() => {
            console.log("Successfully sent the registration token(" + accountType + ", " + token + ") to " + dest);
            return {
                result: "success"
            }
        });
    }
    return {
        result: "error", 
        error : "not logged in"
    }
});

exports.sendGradeChangedNotificationToStudent = functions.database.ref('/dev/Students/{uid}/grades').onWrite(event=>{
    console.log(event);
    console.log(event.before._data);
    console.log(event.after._data);
    console.log(event.params.uid);

    var users = admin.database().ref(`/dev/Users`);
    return users.once("value", function(snapshot){
        console.log(snapshot);
   }, function (errorObject) {
       console.log("The read failed: " + errorObject.code);
   });
});