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