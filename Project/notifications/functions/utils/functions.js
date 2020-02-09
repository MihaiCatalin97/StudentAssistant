exports.getCompleteActionString = (action) => {
    if (action === "added") {
        return `Added to`;
    }
    return `Removed from`;
}