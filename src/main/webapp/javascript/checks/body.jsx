import React, {Component} from "react";
import Delay from "./delay";
import Name from "./name";
import moment from "moment";

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

let Actions = (props) => {
    return <div className="text-right">
        <button className="btn btn-info btn-sm"
                disabled={props.processing}
                onClick={props.delete}>Delete
        </button>
    </div>
}

export default class Body extends Component {
    render() {
        if (this.props.selected) {
            return (
                <div className="card-body">
                    <Name check={this.props.check}
                          updateCheck={this.props.updateCheck}/>
                    <Delay check={this.props.check}
                           updateCheck={this.props.updateCheck}/>
                    <Actions {...this.props}/>
                </div>);
        }

        const {id, name, url} = {...this.props.check};

        let status = <CheckStatus status={this.props.status}
                                  since={this.props.statusSince}/>

        return (
            <div className="card-body">
                {name ?
                    <span><strong>{name}</strong>{status}<br/><small>{url}</small></span>
                    :
                    <span><strong>{url}</strong>{status}</span>}
            </div>);
    }
}