import React, {Component} from "react";
import Checks from "./checks";

const DisabledAccountInfo = (props) => {
    return (
        <div className="container">
            <p>Please contact the owner to enable the
                account.</p>
        </div>);
}

class PingButton extends Component {
    state = {processing: false};

    ping = () => {
        this.setState({processing: true});
        fetch('/ping',
            {credentials: 'same-origin'})
            .then((response) => {
                this.setState({processing: false});
                this.props.fetchChecks();
            });
    }

    render() {
        return <button className="btn btn-info" disabled={this.state.processing}
                       onClick={this.ping}>Ping</button>
    }
}

class RefreshButton extends Component {
    refresh = () => {
        this.props.fetchChecks();
    }

    render() {
        return <button className="btn btn-info" onClick={this.refresh}>
            Refresh</button>
    }
}

class TestButton extends Component {
    state = {processing: false, ready: false};

    switch = () => {
        this.setState({processing: true});
        fetch('/test',
            {
                credentials: 'same-origin',
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "up": (this.state.up ? false : true)
                })
            })
            .then((response) => {
                return response.json();
            })
            .then((responseData) => {
                this.setState({processing: false, up: responseData.up});
            });
    }

    fetchState = () => {
        fetch('/test',
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({up: responseData.up, ready: true});
            });
    }

    componentDidMount() {
        this.fetchState();
    }

    render() {
        return <button
            className={"btn btn-" + (this.state.ready ? (this.state.up ? "success" : "danger") : "info")}
            disabled={this.state.processing}
            onClick={this.switch}>Test</button>
    }
}

export default class App extends Component {
    state = {checks: [], enabled: true, admin: false, logoutUrl: "/"};

    fetchChecks = () => {
        fetch('/api/checks',
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

    addCheck = (check) => {
        this.setState(previous => {
            var checks = previous.checks;
            checks.push(check)
            return {checks: checks};
        });
    }

    updateCheck = (check) => {
        this.setState(previous => {
            var checks = previous.checks;
            console.log(checks, check)
            return {checks: checks.map(it => it.id === check.id ? check : it)};
        });
    }

    deleteCheck = (id) => {
        this.setState(previous => {
            var checks = previous.checks;
            return {checks: checks.filter(check => check.id != id)};
        });
    }

    render() {
        return (
            <div>
                <div className="container">
                    <h1>ping</h1>
                </div>
                <div className="container">
                    <Checks checks={this.state.checks} addCheck={this.addCheck}
                            deleteCheck={this.deleteCheck}
                            updateCheck={this.updateCheck}/>
                </div>
                {this.state.enabled ? '' : <DisabledAccountInfo/>}
                <div className="container">
                    {this.state.admin ? <span className="float-right">
                        <a href={this.state.logoutUrl} className="btn btn-info"
                           role="button">Log out</a>
                    </span> : ''}
                    {this.state.admin ?
                        <a href="/admin" className="btn btn-info"
                           role="button">Admin</a> : ''}
                    {this.state.admin ?
                        <PingButton
                            fetchChecks={this.fetchChecks}/> : ''}
                    {this.state.admin ? <TestButton/> : ''}
                    <RefreshButton fetchChecks={this.fetchChecks}/>
                </div>
            </div>
        );
    }
}
