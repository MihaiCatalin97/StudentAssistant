const admin = require('firebase-admin');

exports.getById = fileMetadataKey => {
    return admin.database().ref(`/dev/Files/metadata/${fileMetadataKey}`).once("value")
        .then(snapshot => {
            fileMetadata = snapshot.val();
            return fileMetadata;
        })
        .catch(error => {
            console.log(`Error while reading file metadata with key ` + fileMetadataKey + `: `, error)
        });
}