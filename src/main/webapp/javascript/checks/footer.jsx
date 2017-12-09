import React, {Component} from "react";
import utils from "../utils";
import moment from "moment";
import ErrorBoundary from "../error-boundary"
import Outages from "./outages";


export default class Footer extends Component {
    state = {fetched: null}

    componentWillUnmount() {
        if (this.loading) {
            this.loading.cancel();
        }
    }

    componentDidUpdate() {
        tooltips();
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.render && (!this.state.fetched || this.state.fetched.isBefore(this.props.refreshed))) {
            this.fetchOutages();
        }
    }

    fetchOutages() {
        this.loading = utils.makeCancelable(fetch('/api/checks/' + this.props.checkId + "/outages",
            {credentials: 'same-origin'}))
        this.loading
            .promise
            .then((response) => response.json())
            .then((responseData) => {
                    this.setState({outages: responseData, fetched: moment()});
                }
            )
            .catch((reason) => console.log('checks fetch canceled ', reason.isCanceled));
    }

    render() {
        if (!this.props.render) {
            return null;
        }
        if (this.state.fetched && (!this.state.outages.length)) {
            return null;
        }
        return (
            <div className="card-footer">
                {!this.state.fetched &&
                <div className="text-center">
                    <img src="/images/ajax-loader.gif"/>
                </div>}
                {this.state.outages &&
                <ErrorBoundary>
                    <Outages outages={this.state.outages}/>
                </ErrorBoundary>}
            </div>
        );
    }
}
