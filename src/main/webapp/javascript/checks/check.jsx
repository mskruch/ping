import React, {Component} from "react";
import moment from "moment";
import utils from "../utils";

export default class Check extends Component {
    state = {
        processing: false,
        edit: false,
        name: this.props.name,
        notificationDelay: this.props.notificationDelay
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

    edit = () => {
        this.setState({edit: true});
    }
    save = () => {
        this.setState({edit: false, processing: true});

        fetch('/api/checks/' + this.props.id,
            {
                credentials: 'same-origin',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "name": this.state.name,
                    "notificationDelay": this.state.notificationDelay
                })
            })
            .then((response) => {
                if (response.status != 200) {
                    throw new Error('check not saved');
                }
                return response.json();
            })
            .then((responseData) => {
                this.props.updateCheck(responseData);
                this.setState({processing: false});
            }).catch((error) => {
                console.error(error);
                this.setState({processing: false});
            }
        );
    }
    cancel = () => {
        this.setState({
                processing: false,
                edit: false,
                name: this.props.name,
                notificationDelay: this.props.notificationDelay
            }
        );
    }

    formatDuration = (seconds) => {
        return seconds ? moment.duration(seconds, 'seconds').humanize() : '';
    }

    rowClicked = () => {
        if (this.props.admin) {
            this.props.select(this.props.check);
        }
    }

    render() {
        const {id, name, url} = {...this.props.check};

        var status = this.props.status ?
            <span
                className={"badge badge-" + (this.props.status === "UP" ? "success" : "danger")}>
            {this.props.status + ' since ' + moment.duration(moment().diff(this.props.statusSince)).humanize()}
        </span> : '';

        return (
            <tr onClick={this.rowClicked} style={{cursor: 'pointer'}}>
                <th scope="row">{this.props.number}</th>
                <td>{this.state.edit ?
                    <input name="name" className="form-control"
                           value={this.state.name}
                           onChange={utils.handleInputChange(this)}
                           placeholder="Name"/> : name}
                </td>
                <td>{url}</td>
                <td>{this.state.edit ?
                    <input name="notificationDelay" className="form-control"
                           value={this.state.notificationDelay}
                           onChange={utils.handleInputChange(this)}
                           placeholder="Seconds"/> : this.formatDuration(this.props.notificationDelay)}
                </td>
                <td>
                    <button className="btn btn-info btn-sm"
                            onClick={this.toggleOutages}>
                        <i className={"fa fa-caret-square-o-" + (this.props.outages ? "up" : "down")}></i>
                    </button>
                    {status}
                </td>
                <td className="action">
                    <span className={this.state.edit ? "" : "actionspan"}>
                {!this.state.edit ?
                    <button className="btn btn-primary btn-sm"
                            onClick={this.edit}
                            disabled={this.state.processing}>
                        Edit
                    </button> : ''}
                        {!this.state.edit ?
                            <button className="btn btn-danger btn-sm"
                                    onClick={this.delete}
                                    disabled={this.state.processing}>
                                Delete
                            </button> : ''}
                        {this.state.edit ?
                            <button className="btn btn-primary btn-sm"
                                    onClick={this.save}
                                    disabled={this.state.processing}>
                                Save
                            </button> : ''}
                        {this.state.edit ?
                            <button className="btn btn-secondary btn-sm"
                                    onClick={this.cancel}
                                    disabled={this.state.processing}>
                                Cancel
                            </button> : ''}
                    </span>
                </td>
            </tr>
        );
    }
}
