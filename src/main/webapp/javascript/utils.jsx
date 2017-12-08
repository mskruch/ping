var saveInputChangeToState = (event, component) => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;

    component.setState({
        [name]: value
    });
}

exports.handleInputChange = (component) => {
    return function (event) {
        return saveInputChangeToState(event, component);
    }
}

exports.makeCancelable = (promise) => {
    let hasCanceled_ = false;

    const wrappedPromise = new Promise((resolve, reject) => {
        promise.then(
            val => hasCanceled_ ? reject({isCanceled: true}) : resolve(val),
            error => hasCanceled_ ? reject({isCanceled: true}) : reject(error)
        );
    });

    return {
        promise: wrappedPromise,
        cancel() {
            hasCanceled_ = true;
        },
    };
};

exports.isValidUrl = (value) => {
    var urlPattern = /(http|ftp|https):\/\/[\w-]+(\.[\w-]+)*([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/
    return urlPattern.test(value);
}