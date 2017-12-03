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

const Outages = (props) => {
    return (
        <small>
            <table className="table table-sm outages">
                <tbody>
                {props.outages.map((outage, i) =>
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

class Footer extends Component {
    state = {outages: null}

    componentDidMount() {
        fetch('/api/checks/' + this.props.checkId + "/outages",
            {credentials: 'same-origin'})
            .then((response) => response.json())
            .then((responseData) => {
                    this.setState({outages: responseData});
                }
            );
    }

    render() {
        if (this.state.outages != null && this.state.outages.length == 0){
            return null;
        }
        return <div className="card-footer text-center">
            {this.state.outages == null ?
                <img src="/images/ajax-loader.gif"/> :
                <Outages outages={this.state.outages}/>}
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

    toggleOutages = () => {
        this.props.toggleOutages(this.props.id);
    }

    clicked = () => {
        if (!this.props.selected) {
            this.props.select(this.props.check);
        }
    }

    render() {
        return (
            <div onClick={this.clicked}
                 style={this.props.selected ? {} : {cursor: 'pointer'}}
                 className={"card check" + (this.props.selected ? "selected" : "") + " mb-3"}>
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
                {this.props.selected ?
                    <Footer render={this.props.selected}
                            checkId={this.props.check.id}/> :
                    null}

            </div>
        );

    }
}
