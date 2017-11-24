import React, {Component} from "react";

const Check = (props) => {
    return (
        <div>
            {props.url}
        </div>
    );
}

const CheckList = (props) => {
    var checks = props.checks.map((check, id) =>
        <Check key={id} url={check.url}/>
    );

    return (
        <div>
            {checks}
        </div>
    );
}

export default class App extends Component {
    state = {checks: []};

    fetchChecks() {
        fetch('/checks',
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    checks: responseData,
                });
            });
    }

    componentDidMount() {
        this.fetchChecks();
    }

    render() {
        return (
            <div>
                <CheckList checks={this.state.checks}/>
            </div>
        );
    }
}
