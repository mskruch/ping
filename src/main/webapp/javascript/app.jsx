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
        selected: null,
        debug: false,
        ready: false,
        fetched: moment(),
        session: {enabled: true, admin: false, logoutUrl: "/", checksLimit: null}
    };

    fetchChecks = () => {
        this.setState({
            fetched: moment()
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

    fetchSession() {
        fetch('/api/session',
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                    this.setState({session: responseData});
                }
            );
    }

    componentDidMount() {
        this.fetchSession();
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
                {this.state.session.enabled || <DisabledAccountInfo/>}

                <Checks checks={this.state.checks}
                        deleteCheck={this.deleteCheck}
                        updateCheck={this.updateCheck}
                        admin={this.state.session.admin}
                        select={this.select}
                        selected={this.state.selected}
                        ready={this.state.ready}
                        fetched={this.state.fetched}/>


                <AddCheck addCheck={this.addCheck}
                          checksCount={this.state.checks.length}
                          checksLimit={this.state.session.checksLimit}/>

                {this.state.debug &&
                <DebugButtons admin={this.state.session.admin}
                              fetchChecks={this.fetchChecks}
                              logoutUrl={this.state.session.logoutUrl}/>}
            </div>
        );
    }
}