const nodemailer = require('nodemailer');
const cors = require('cors')({ origin: true });

const config = {
    email: 'student.assistant00@gmail.com',
    password: 'Studentassistant12'
}

let transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: config.email,
        pass: config.password
    }
});

exports.sendInvitationEmail = (to, accountType, token, expiresAt) => {
    const mailOptions = {
        from: 'Student Assistant <student.assistant00@gmail.com>',
        to: dest,
        subject: 'Student Assistant - Invitation',
        html: `<p style="font-size: 18px;">Hello, ` + accountType + `</p>
                    <br />
                    <p style="font-size: 16px;">You have been invited to create an ` + accountType + ` account in the Student Assistant application.</p>
                    <p style="font-size: 16px;">To register, please use this token: ` + token + `</p>
                    <p style="font-size: 16px;">Beware, this is a confidential, single-use token and it will expire at ` + expiresAt + `</p>
                    <p style="font-size: 16px;"><b>Download the app from:</b></p>\n
                    <a">(app store link)</a>
                `
    };

    return sendEmail(mailOptions)
        .catch((error) => {
            console.log("An error occurred while sending the registration token(" + accountType + ", " + token + ") to " + dest + ":\n" + error);

            return {
                result: "error",
                error: error
            }
        })
        .then(() => {
            console.log("Successfully sent the registration token(" + accountType + ", " + token + ") to " + dest);
            return {
                result: "success"
            }
        });
}

const sendEmail = (mailOptions) => {
    return transporter.sendMail(mailOptions);
}
