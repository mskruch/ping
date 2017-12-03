import React, {Component} from "react";
import moment from "moment";
import utils from "../utils";
import Body from "./body";

const Outages = (props) => {
    return (
        <small>
            <table className="table table-sm outages">
                <tbody>
                {props.outages.map((outage, i) =>
                    <tr key={i} className={outage.finished ? '' : 'down'}>
                        <td width="50%">{moment(outage.started).format('YYYY-MM-DD HH:mm')}</td>
                        <td width="30%">{outage.finished ? moment(outage.finished).from(outage.started, true) : null}</td>
                        <td width="20%">{outage.notified ? 'notified' : ''}</td>
                    </tr>
                )}
                </tbody>
            </table>
        </small>
    );
}

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
            return <span className="status badge badge-danger">Something went wrong</span>;
        }
        return this.props.children;
    }
}

class Footer extends Component {
    state = {outages: null}

    componentDidMount() {
    }

    componentWillUnmount() {
        if (this.loading) {
            this.loading.cancel();
        }
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.render && this.state.outages == null) {
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
                    this.setState({outages: responseData});
                }
            )
            .catch((reason) => console.log('checks fetch canceled ', reason.isCanceled));
    }

    render() {
        if (!this.props.render) {
            return null;
        }
        if (this.state.outages != null && this.state.outages.length == 0) {
            return null;
        }
        return <div className="card-footer text-center">
            {this.state.outages == null ?
                <img src="/images/ajax-loader.gif"/> :
                <ErrorBoundary><Outages
                    outages={this.state.outages}/></ErrorBoundary>}
        </div>
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
                        checkId={this.props.check.id}/>
            </div>
        );

    }
}
