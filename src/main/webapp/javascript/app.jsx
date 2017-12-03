import React, {Component} from "react";
import moment from "moment";
import Checks from "./checks";

const DisabledAccountInfo = (props) => {
    return (
        <div className="container">
            <p>Please contact the owner to enable the
                account.</p>
        </div>
    );
}

const Outages = (props) => {
    if (!props.checkId) {
        return null;
    }
    return (
        <div className="container">
            <h3>Outages</h3>
            <table className="table">
                <tbody>
                {props.outages.map((outage, i) =>
                    <tr key={i}>
                        <td width="35%">{moment(outage.started).format('YYYY-MM-DD HH:mm')}</td>
                        <td width="35%">{outage.finished ? moment(outage.finished).from(outage.started, true) :
                            <span
                                className="badge badge-danger">outage</span>}</td>
                        <td width="30%">{outage.notified ? 'notified ' + moment(outage.notified).format('YYYY-MM-DD HH:mm') : ''}</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
        ;
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
    state = {
        checks: [],
        enabled: true,
        admin: false,
        logoutUrl: "/",
        outages: [],
        outagesCheckId: null,
        selected: null
    };

    fetchChecks = () => {
        fetch('/api/checks',
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    checks: responseData,
                });
                tooltips();
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
        // this.intervalId = setInterval(this.fetchChecks, 2000);
        document.body.addEventListener('click', this.handleClickOutside);
    }

    componentWillUnmount() {
        // clearInterval(this.intervalId);
        document.body.removeEventListener('click', this.handleClickOutside);

    }

    handleClickOutside = (event) => {
        if (!this.state.selected) {
            return;
        }

        let checks = document.querySelector('.check');
        let selected = document.querySelector('.check-selected');

        if (checks && checks.contains(event.target)) {
            // console.log('clicked not selected check');
        } else if (selected && selected.contains(event.target)){
            // console.log('clicked selected check');
        } else {
            // console.log('clicked outside');
            this.select(null);
        }
    }

    addCheck = (check) => {
        this.setState(previous => {
            var checks = previous.checks;
            if (!checks.find(it => it.id === check.id)) {
                checks.push(check)
            }
            return {checks: checks};
        });
    }

    updateCheck = (check) => {
        this.setState(previous => {
            var checks = previous.checks;
            return {checks: checks.map(it => it.id === check.id ? check : it)};
        });
    }

    deleteCheck = (id) => {
        this.setState(previous => {
            var checks = previous.checks;
            return {checks: checks.filter(check => check.id != id)};
        });
    }

    toggleOutages = (id) => {
        this.setState(previous => {
            return {
                outagesCheckId: id != previous.outagesCheckId ? id : null,
                outages: []
            }
        });
        fetch('/api/checks/' + id + "/outages",
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                    this.setState({outages: responseData});
                }
            );
    }

    select = (check) => {
        this.setState({
            selected: check
        })
    }

    render() {
        return (
            <div>
                {this.state.enabled ? '' : <DisabledAccountInfo/>}
                <Checks checks={this.state.checks} addCheck={this.addCheck}
                        deleteCheck={this.deleteCheck}
                        updateCheck={this.updateCheck}
                        outagesCheckId={this.state.outagesCheckId}
                        toggleOutages={this.toggleOutages}
                        admin={this.state.admin}
                        select={this.select}
                        selected={this.state.selected}/>
                <div className="container">
                    {this.state.admin ? <span className="float-right">
                        <a href={this.state.logoutUrl} className="btn btn-info"
                           role="button">Log out</a>
                    </span> : ''}
                    &nbsp;
                    {this.state.admin ?
                        <a href="/admin" className="btn btn-info"
                           role="button">Admin</a> : ''}
                    &nbsp;
                    {this.state.admin ?
                        <PingButton
                            fetchChecks={this.fetchChecks}/> : ''}
                    &nbsp;
                    {this.state.admin ? <TestButton/> : ''}
                    &nbsp;
                    <RefreshButton fetchChecks={this.fetchChecks}/>
                </div>
                <Outages outages={this.state.outages}
                         checkId={this.state.outagesCheckId}/>
            </div>
        );
    }
}