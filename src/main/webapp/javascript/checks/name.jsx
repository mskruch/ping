import React, {Component} from "react";
import utils from "../utils";

let LabelUrl = (props) => {
    return <small>{props.url}</small>
};

let LabelName = (props) => {
    return (
        <span
            className={props.name ? '' : 'text-muted'}>{props.name ?
            <strong>{props.name}</strong> : 'Unnamed'}
            <span> </span>
            <a href="javascript:void(0)"><i
                className="fa fa-pencil-square-o edit"
                onClick={props.edit}
                style={{cursor: 'pointer'}}></i></a>
        </span>);
};

export default class Name extends Component {
    state = {processing: false}

    edit = () => {
        this.props.edit();
    };

    save = (value) => {
        this.setState({processing: true});
        fetch('/api/checks/' + this.props.check.id,
            {
                credentials: 'same-origin',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "name": value
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
                this.props.edit(false);
                this.setState({processing: false});
            }).catch((error) => {
                console.error(error);
                this.props.edit(false);
                this.setState({processing: false});
            }
        );
    }

    cancel = () => {
        this.props.edit(false);
    }

    render() {
        const {id, name, url} = {...this.props.check};

        return (
            <span className="selected-check-property">
                {this.props.editing ?
                    <InlineTextInput style={{maxWidth: '30em'}}
                                     value={this.props.check.name}
                                     placeholder="Name"
                                     save={this.save}
                                     disabled={this.state.processing}
                                     cancel={this.cancel}/>
                    :
                    <div><LabelName edit={this.edit}
                                    name={name}/>{this.props.status}</div>
                }
                <div><LabelUrl url={url}/></div>
            </span>);
    }
}

class InlineTextInput extends Component {
    state = {value: this.props.value || ''}

    save = () => {
        this.props.save(this.state.value);
    }

    componentDidMount() {
        // this.input.focus();
        this.input.select();
    }

    setInput = (input) => {
        this.input = input;
    }

    onKeyDown = (event) => {
        if (event.key == 'Enter') {
            this.save();
        } else if (event.key == 'Escape') {
            this.props.cancel();
        }
    }

    render() {
        return (
            <div className="input-group mb-2 mb-sm-0">
                <input name="value" ref={this.setInput}
                       className="form-control form-control-sm"
                       style={this.props.style}
                       value={this.state.value}
                       onChange={utils.handleInputChange(this)}
                       placeholder={this.props.placeholder}
                       onKeyDown={this.onKeyDown}/>
                <div className="input-group-addon"
                     style={{cursor: 'pointer'}} onClick={this.save}
                     disabled={this.props.disabled}>
                    <i className="fa fa-check"></i></div>
            </div>
        );
    }
}