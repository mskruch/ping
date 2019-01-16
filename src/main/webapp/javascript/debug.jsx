import React, {Component} from "react";


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

export default class DebugButtons extends Component {
    render() {
        return (
            <div className="container">
                {this.props.admin &&
                <span>
                <span className="float-right">
                        <a href={this.props.logoutUrl} className="btn btn-info"
                           role="button">Log out</a>
                </span> <a href="/admin" className="btn btn-info"
                           role="button">Admin</a> <button onClick={this.props.switchAdmin} className="btn btn-info" role="button">Admin.</button> <PingButton
                    fetchChecks={this.props.fetchChecks}/> <TestButton/>
                </span>} <RefreshButton fetchChecks={this.props.fetchChecks}/>
            </div>
        );
    }
}