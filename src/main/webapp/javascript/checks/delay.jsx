import React, {Component} from "react";
import utils from "../utils";
import moment from "moment";


let formatDuration = (seconds) => {
    return seconds ? moment.duration(seconds, 'seconds').humanize() : '';
}

let LabelDelay = (props) => {
    let button = <a href="javascript:void(0)"><i
        className="fa fa-pencil-square-o edit"
        onClick={props.edit}
        style={{cursor: 'pointer'}}></i></a>
    if (props.delay) {
        return <span>Notification will be sent after {formatDuration(props.delay)} {button}
            &nbsp;of outage</span>
    } else {
        return <span
            className="text-muted">Notification delay is not set {button}</span>
    }
};

export default class Delay extends Component {
    state = {edit: false, processing: false}

    edit = () => {
        this.setState({edit: true, delay: this.props.check.notificationDelay});
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
                    "notificationDelay": this.state.delay
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
        return (
            <div className="selected-check-property">
                {this.state.edit ?
                    <div className="form-inline">
                        <label>Notification will be sent after</label>&nbsp;
                        <div className="input-group">
                            <input name="delay"
                                   className="form-control form-control-sm"
                                   style={{maxWidth: '6em'}}
                                   value={this.state.delay}
                                   onChange={utils.handleInputChange(this)}
                                   placeholder="seconds"/>
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
                        &nbsp;<label>of outage</label>
                    </div>
                    :
                    <div><LabelDelay edit={this.edit}
                                     delay={this.props.check.notificationDelay}/>
                    </div>
                }
            </div>);
    }
}