import React, {Component} from "react";
import utils from "../utils";
import Outages from "./outages";
import Body from "./body";
import moment from "moment";

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {hasError: false};
    }

    componentDidCatch(error, info) {
        this.setState({hasError: true});
        console.log(error, info);
    }

    render() {
        if (this.state.hasError) {
            return <div className="text-center"><span
                className="status badge badge-danger">Something went wrong</span>
            </div>;
        }
        return this.props.children;
    }
}

class Footer extends Component {
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

export default class Check extends Component {
    state = {
        processing: false,
    };

    delete = () => {
        this.setState({processing: true})
        fetch('/api/checks/' + this.props.id,
            {
                credentials: 'same-origin',
                method: 'delete'
            })
            .then((response) => {
                if (response.status >= 400) {
                    throw new Error('check not deleted');
                }
                this.setState({processing: false});
                this.props.deleteCheck(this.props.id);
            }).catch((error) => {
                console.error(error);
                this.setState({processing: false});
            }
        );
    }

    pause = () => {
        this.patchPaused(true);
    }

    resume = () => {
        this.patchPaused(false);
    }

    patchPaused = (value) => {
        this.setState({processing: true});
        fetch('/api/checks/' + this.props.check.id,
            {
                credentials: 'same-origin',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "paused": value
                })
            })
            .then((response) => {
                return response.json();
            })
            .then((responseData) => {
                this.props.updateCheck(responseData);
                this.setState({processing: false});
            });
    }

    clicked = () => {
        if (!this.props.selected) {
            this.props.select(this.props.check);
        }
    }

    render() {
        let className = "card mb-3 check"
            + (this.props.selected ? "-selected" : "")
            + (this.props.paused ? " text-muted" : "");
        return (
            <div onClick={this.clicked}
                 style={this.props.selected ? {} : {cursor: 'pointer'}}
                 className={className}>
                <Body check={this.props.check}
                      status={this.props.status}
                      statusSince={this.props.statusSince}
                      updateCheck={this.props.updateCheck}
                      selected={this.props.selected}
                      delete={this.delete}
                      pause={this.pause}
                      resume={this.resume}
                      processing={this.processing}/>
                <Footer render={this.props.selected}
                        checkId={this.props.check.id}
                        refreshed={this.props.refreshed}/>
            </div>
        );

    }
}
