import React, {Component} from "react";
import utils from "../utils";

export default class CheckAdd extends Component {
    initialState = {processing: false, url: '', urlError: ''};
    state = this.initialState;

    validate = () => {
        if (utils.isValidUrl(this.state.url)) {
            this.setState({urlError: ''});
            return true;
        }
        this.setState({urlError: 'Invalid URL.'});
        return false;
    }

    onKeyDown = (event) => {
        if (event.key == 'Enter') {
            this.submit();
        }
    }

    submit = () => {
        if (!this.validate()) {
            return;
        }
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
        let limitReached = this.props.checksLimit && this.props.checksCount >= this.props.checksLimit;
        let disabled = limitReached || this.state.processing;
        return (
            <div className="container">
                <div className="card">
                    <div className="card-body">
                        <div className="row">
                            <div className="col-sm-10">
                                <input name="url"
                                       className={"form-control " + (this.state.urlError && "is-invalid")}
                                       value={this.state.url}
                                       onChange={utils.handleInputChange(this)}
                                       placeholder="http://google.com"
                                       onKeyDown={this.onKeyDown}
                                       disabled={disabled}/>
                                {this.state.urlError &&
                                <div className="invalid-feedback">
                                    {this.state.urlError}
                                </div>}
                            </div>
                            <div className="col-sm-2">
                                <button onClick={this.submit}
                                        className="btn btn-primary btn-block"
                                        disabled={disabled}>
                                    Add
                                </button>
                            </div>
                        </div>
                        {limitReached &&
                        <small>
                            You have reached the maximum checks limit ({this.props.checksLimit}).
                        </small>}
                    </div>
                </div>
            </div>
        );
    }
}
