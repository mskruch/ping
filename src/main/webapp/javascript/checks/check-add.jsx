import React, {Component} from "react";
import utils from "../utils";

export default class CheckAdd extends Component {
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
