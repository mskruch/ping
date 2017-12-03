import React, {Component} from "react";
import utils from "../utils";

let LabelUrl = (props) => {
    return <small>{props.url}</small>
};

let LabelName = (props) => {
    return (
        <span
            className={props.name ? '' : 'text-muted'}>{props.name ? <strong>{props.name}</strong> : 'Unnamed'}
            <span> </span>
            <a href="javascript:void(0)"><i
                className="fa fa-pencil-square-o edit"
                onClick={props.edit}
                style={{cursor: 'pointer'}}></i></a>
        </span>);
};

export default class Name extends Component {
    state = {edit: false, processing: false}

    edit = () => {
        this.setState({edit: true, name: this.props.check.name});
    };

    cancel = () => {
        this.setState({edit: false});
    }

    save = () => {
        this.setState({processing: true});
        fetch('/api/checks/' + this.props.check.id,
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
                this.setState({processing: false, edit: false});
            }).catch((error) => {
                console.error(error);
                this.setState({processing: false, edit: false});
            }
        );
    }

    render() {
        const {id, name, url} = {...this.props.check};

        return (
            <span className="selected-check-property">
                {this.state.edit ?
                    <div className="input-group mb-2 mb-sm-0">
                        <input name="name" className="form-control form-control-sm"
                               style={{maxWidth: '30em'}}
                               value={this.state.name}
                               onChange={utils.handleInputChange(this)}
                               placeholder="Name"/>
                        <div className="input-group-addon"
                             style={{cursor: 'pointer'}} onClick={this.save}
                             disabled={this.state.processing}>
                            <i className="fa fa-check"></i></div>
                        <div className="input-group-addon"
                             style={{cursor: 'pointer'}}
                             onClick={this.cancel}
                             disabled={this.state.processing}>
                            <i className="fa fa-times"></i></div>
                    </div>
                    :
                    <div><LabelName edit={this.edit} name={name}/></div>
                }
                <div><LabelUrl url={url}/></div>
            </span>);
    }
}