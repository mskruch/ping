import React, {Component} from "react";
import moment from "moment";
import Checks from "./checks";
import DebugButtons from "./debug";

const DisabledAccountInfo = (props) => {
    return (
        <div className="container">
            <p>The account is not active and must be verified</p>
        </div>
    );
}

export default class App extends Component {
    state = {
        checks: [],
        enabled: true,
        admin: false,
        logoutUrl: "/",
        selected: null,
        debug: false,
        ready: false
    };

    fetchChecks = () => {
        fetch('/api/checks',
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    checks: responseData,
                    ready: true
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
        // this.intervalId = setInterval(this.fetchChecks, 2000);
        document.body.addEventListener('click', this.handleClickOutside);
        this.setState({debug: window.location.hostname === 'localhost'});
    }

    componentWillUnmount() {
        // clearInterval(this.intervalId);
        document.body.removeEventListener('click', this.handleClickOutside);
    }

    componentDidUpdate() {
        tooltips();
    }

    handleClickOutside = (event) => {
        if (!this.state.selected) {
            return;
        }

        let checks = document.querySelector('.check');
        let selected = document.querySelector('.check-selected');

        if (checks && checks.contains(event.target)) {
            // console.log('clicked not selected check');
        } else if (selected && selected.contains(event.target)) {
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

    select = (check) => {
        hideTooltips();
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
                        admin={this.state.admin}
                        select={this.select}
                        selected={this.state.selected}
                        ready={this.state.ready}/>

                {this.state.debug &&
                <DebugButtons admin={this.state.admin}
                              fetchChecks={this.fetchChecks}
                              logoutUrl={this.state.logoutUrl}/>}
            </div>
        );
    }
}