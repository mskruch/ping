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

const DisabledAccountInfo = (props) => {
    return (
        <div className="container">
            <p>Please contact the owner to enable the
                account.</p>
        </div>);
}

export default class App extends Component {
    state = {checks: [], enabled: true};

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

    fetchState() {
        fetch('/api/state',
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                    this.setState(responseData);
                }
            );
    }

    componentDidMount() {
        this.fetchState();
        this.fetchChecks();
    }

    render() {
        return (
            <div>
                <div className="container">
                    <h1>ping</h1>
                </div>
                <div className="container">
                    <CheckList checks={this.state.checks}/>
                </div>
                {this.state.enabled ? '' : <DisabledAccountInfo/>}
                <div className="container">
                    <span className="float-right">
                        <a href={this.state.logoutUrl} className="btn btn-info"
                           role="button">Log out</a>
                    </span>
                    {this.state.admin ?
                        <a href="/admin" className="btn btn-info"
                           role="button">Admin</a> : ''}
                </div>
            </div>
        );
    }
}
