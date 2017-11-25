import React, {Component} from "react";
import moment from "moment";
import utils from "./utils";


class Check extends Component {
    state = {processing: false, edit: false, name: this.props.name}

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
                    "name": this.state.name
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
        this.setState({edit: false, name: this.props.name});
    }

    render() {
        var status = this.props.status ?
            <span
                className={"badge badge-" + (this.props.status === "UP" ? "success" : "danger")}>
            {this.props.status + ' since ' + moment.duration(moment().diff(this.props.statusSince)).humanize()}
        </span> : '';

        return (
            <tr>
                <th scope="row">{this.props.number}</th>
                <td>{this.state.edit ?
                    <input name="name" className="form-control"
                           value={this.state.name}
                           onChange={utils.handleInputChange(this)}
                           placeholder="Enter name"/> : this.props.name}</td>
                <td>{this.props.url}</td>
                <td>{status}</td>
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

class CheckForm extends Component {
    initialState = {processing: false, name: '', url: '', validated: false};
    state = this.initialState;

    submit = () => {
        this.setState({processing: true});
        fetch('/api/checks',
            {
                credentials: 'same-origin',
                method: 'post',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "name": this.state.name,
                    "url": this.state.url
                })
            })
            .then((response) => {
                if (response.status != 200) {
                    throw new Error('check not created');
                }
                return response.json();
            })
            .then((responseData) => {
                this.props.addCheck(responseData);
                this.setState(this.initialState);
            }).catch((error) => {
                console.error(error);
                this.setState({processing: false, validated: true});
            }
        );
    };

    render() {
        return (
            <tr>
                <th scope="row"></th>
                <td><input name="name"
                           className={"form-control " + (this.state.validated ? "is-valid" : "")}
                           value={this.state.name}
                           onChange={utils.handleInputChange(this)}
                           placeholder="Enter name"/></td>
                <td><input name="url"
                           className={"form-control " + (this.state.validated ? "is-invalid" : "")}
                           value={this.state.url}
                           onChange={utils.handleInputChange(this)}
                           placeholder="Enter url"/>
                    {this.state.validated ?
                        <div class="invalid-feedback">
                            Url is required
                        </div> : ''}
                </td>
                <td></td>
                <td className="action">
                    <button onClick={this.submit} className="btn btn-primary"
                            disabled={this.state.processing}>Add
                    </button>
                </td>
            </tr>
        );
    }
}

export default class Checks extends Component {
    render() {
        var checks = this.props.checks.map((check, i) =>
            <Check key={i} number={i + 1} {...check}
                   deleteCheck={this.props.deleteCheck}
                   updateCheck={this.props.updateCheck}/>
        );

        return (
            <table className="table table-hover">
                <thead>
                <tr>
                    <th scope="col action" width="0%">#</th>
                    <th scope="col" width="20%">Name</th>
                    <th scope="col" width="40%">Url</th>
                    <th scope="col" width="20%">Status</th>
                    <th scope="col action" width="20%"></th>
                </tr>
                </thead>
                <tbody>
                {checks}
                <CheckForm addCheck={this.props.addCheck}/>
                </tbody>
            </table>
        );
    }
}
