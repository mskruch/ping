import React, {Component} from "react";
import moment from "moment";

const Check = (props) => {
    var status = props.status ?
        <span
            className={"badge badge-" + (props.status == "UP" ? "success" : "danger")}>
            {props.status + ' since ' + moment.duration(moment().diff(props.statusSince)).humanize()}
        </span> : '';

    return (
        <tr>
            <th scope="row">{props.number}</th>
            <td>{props.name}</td>
            <td>{props.url}</td>
            <td>{status}</td>
        </tr>
    );
}

class CheckForm extends Component {
    state = {processing: false, name: '', url: ''}

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

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
            .then((response) => response.json())
            .then((responseData) => {
                this.setState({
                    processing: false,
                    name: '',
                    url: ''
                });
            });
    }

    handleChange(event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }

    render() {
        return (
            <tr>
                <th scope="row"></th>
                <td><input name="name" className="form-control"
                           value={this.state.name} onChange={this.handleChange}
                           placeholder="Enter name"/></td>
                <td><input name="url" className="form-control"
                           value={this.state.url} onChange={this.handleChange}
                           placeholder="Enter url"/></td>
                <td>
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
            <Check key={i} number={i + 1} {...check}/>
        );

        return (
            <table className="table table-hover">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Name</th>
                    <th scope="col">Url</th>
                    <th scope="col">Status</th>
                </tr>
                </thead>
                <tbody>
                {checks}
                <CheckForm/>
                </tbody>
            </table>
        );
    }
}
