import React, {Component} from "react";
import moment from "moment";
import Name from "./name";
import Delay from "./delay";

let CheckStatus = (props) => {
    if (!props.status)
        return null;
    return (
        <span
            className={"status badge badge-" + (props.status === "UP" ? "success" : "danger")}
            data-toggle="tooltip" data-placement="right"
            title={"since " + moment.duration(moment().diff(props.since)).humanize()}>
            {props.status}</span>);
}

class CheckBody extends Component {
    render() {
        const {id, name, url} = {...this.props.check};

        let status = <CheckStatus status={this.props.status}
                                  since={this.props.statusSince}/>

        return (
            <div className="card-body">
                {name ?
                    <span>{name}{status}<br/><small>{url}</small></span>
                    :
                    <span>{url}{status}</span>}
            </div>);
    }
}

class SelectedCheckBody extends Component {
    render() {
        return (
            <div className="card-body">
                <Name check={this.props.check}
                      updateCheck={this.props.updateCheck}/>
                <Delay check={this.props.check}
                       updateCheck={this.props.updateCheck}/>
            </div>);
    }
}

class Outages extends Component {
    state = {outages: [], ready: false}

    componentDidMount() {
        fetch('/api/checks/' + this.props.checkId + "/outages",
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                    this.setState({outages: responseData, ready: true});
                }
            );
    }

    render() {
        return (
            <small>
                <table className="table table-sm outages">
                    <tbody>
                    {this.state.outages.map((outage, i) =>
                        <tr key={i} className={outage.finished ? '' : 'down'}>
                            <td width="35%">{moment(outage.started).format('YYYY-MM-DD HH:mm')}</td>
                            <td width="35%">{outage.finished ? moment(outage.finished).from(outage.started, true) : null}</td>
                            <td width="30%">{outage.notified ? 'notified ' + moment(outage.notified).format('YYYY-MM-DD HH:mm') : ''}</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </small>
        );
    }
}

const Footer = (props) => {
    if (!props.render)
        return null;
    return <div className="card-footer">
        <Outages checkId={props.checkId}/>
    </div>
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

    toggleOutages = () => {
        this.props.toggleOutages(this.props.id);
    }

    rowClicked = () => {
        if (!this.props.selected) {
            this.props.select(this.props.check);
        }
    }

    render() {
        return (
            <div onClick={this.rowClicked}
                 style={this.props.selected ? {} : {cursor: 'pointer'}}
                 className="card check mb-3">
                {this.props.selected ? <div className="card-header">
                    <ul className="nav nav-pills card-header-pills mr-auto">
                        <li className="nav-item mr-auto">
                            <button className="btn btn-danger btn-sm"
                                    disabled={this.state.processing}
                                    onClick={this.delete}>Delete
                            </button>
                        </li>
                        <li className="nav-item ml-auto">
                            <button className="btn btn-info btn-sm"
                                    onClick={() => {
                                        this.props.select(null)
                                    }}>Close
                            </button>
                        </li>
                    </ul>
                </div> : null}
                {this.props.selected ?
                    <SelectedCheckBody check={this.props.check}
                                       status={this.props.status}
                                       statusSince={this.props.statusSince}
                                       updateCheck={this.props.updateCheck}/>
                    : <CheckBody check={this.props.check}
                                 status={this.props.status}
                                 statusSince={this.props.statusSince}/>
                }
                <Footer render={this.props.selected}
                        checkId={this.props.check.id}/>

            </div>
        );

    }
}
