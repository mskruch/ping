import React, {Component} from "react";

const Check = (props) => {
    var status = props.status ?
        <span
            className={"badge badge-" + (props.status == "UP" ? "success" : "danger")}>
            {props.status + ' since ' + props.statusSinceDuration}
        </span> : '';

    return (
        <tr>
            <th scope="row">{props.number}</th>
            <td>{props.name}</td>
            <td>{props.url}</td>
            <td>{status}</td>
        </tr>
    );
}

const CheckList = (props) => {
    var checks = props.checks.map((check, i) =>
        <Check key={i} number={i + 1} {...check}/>
    );

    return (
        <table className="table table-hover">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Name</th>
                <th scope="col">Url</th>
                <th scope="col">Status</th>
            </tr>
            </thead>
            <tbody>
            {checks}
            </tbody>
        </table>
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
            <div className="container">
                <h1>ping</h1>
                <CheckList checks={this.state.checks}/>
            </div>
        );
    }
}
