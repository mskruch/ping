import React, {Component} from "react";
import Checks from "./checks";
import DebugButtons from "./debug";
import AddCheck from "./checks/check-add";
import moment from "moment";

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
        ready: false,
        refreshed: moment()
    };

    fetchChecks = () => {
        this.setState({
            refreshed: moment()
        });

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

    setRef = (ref) => {
        this.appRef = ref;
    }

    handleClickOutside = (event) => {
        if (event.target == this.appRef || event.target.tagName.toUpperCase() == 'BODY') {
            this.select(null);
            return;
        }
        if (event.target.className && event.target.className.indexOf('container') != -1) {
            this.select(null);
            return;
        }
    }

    addCheck = (check) => {
        this.setState(previous => {
            var checks = previous.checks;
            if (!checks.find(it => it.id == check.id)) {
                checks.push(check)
            }
            return {checks: checks};
        });
    }

    updateCheck = (check) => {
        this.setState(previous => {
            return {checks: previous.checks.map(it => it.id == check.id ? check : it)};
        });
    }

    deleteCheck = (id) => {
        this.setState(previous => {
            return {checks: previous.checks.filter(check => check.id != id)};
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
            <div id="app" ref={this.setRef}>
                {this.state.enabled || <DisabledAccountInfo/>}

                <Checks checks={this.state.checks}
                        deleteCheck={this.deleteCheck}
                        updateCheck={this.updateCheck}
                        admin={this.state.admin}
                        select={this.select}
                        selected={this.state.selected}
                        ready={this.state.ready}
                        refreshed={this.state.refreshed}/>


                <AddCheck addCheck={this.addCheck}/>

                {this.state.debug &&
                <DebugButtons admin={this.state.admin}
                              fetchChecks={this.fetchChecks}
                              logoutUrl={this.state.logoutUrl}/>}
            </div>
        );
    }
}