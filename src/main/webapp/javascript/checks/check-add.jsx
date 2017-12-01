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
            <div className="card">
                <div className="card-body">
                    <div className="form-group">
                        <input name="name"
                               className={"form-control " + (this.state.validated ? "is-valid" : "")}
                               value={this.state.name}
                               onChange={utils.handleInputChange(this)}
                               placeholder="Name"/>
                    </div>
                    <div className="form-group">
                        <input name="url"
                               className={"form-control " + (this.state.validated ? "is-invalid" : "")}
                               value={this.state.url}
                               onChange={utils.handleInputChange(this)}
                               placeholder="URL"/>
                        {this.state.validated ?
                            <div className="invalid-feedback">
                                Url is required
                            </div> : ''}
                    </div>
                    <div className="form-group">
                        <button onClick={this.submit} className="btn btn-primary btn-block"
                                disabled={this.state.processing}>Add</button>
                    </div>
                </div>
            </div>
        );
    }
}
