import React, {Component} from "react";
import utils from "../utils";
import moment from "moment";

let formatDuration = (seconds) => {
    return seconds && moment.duration(seconds, 'seconds').humanize();
}

let formatDelay = (seconds) => {
    return seconds ? 'after ' + formatDuration(seconds) : 'instantly';
}

let LabelDelay = (props) => {
    let button = <span> <a href="javascript:void(0)"><i
        className="fa fa-pencil-square-o edit"
        onClick={props.edit}
        style={{cursor: 'pointer'}}></i></a></span>
    if (props.delay) {
        return <span>Notification will be sent after {formatDuration(props.delay)} {button}</span>
    } else {
        return <span
            className="text-muted">Notification will be sent instantly {button}</span>
    }
};

export default class Delay extends Component {
    static defaultProps = {
        options: [0, 300, 600, 1800, 3600, 14400, 86400]
    }

    state = {processing: false}

    save = (value) => {
        this.setState({processing: true});
        fetch('/api/checks/' + this.props.check.id,
            {
                credentials: 'same-origin',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "notificationDelay": value
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

    onChange = (event) => {
        const target = event.target;
        const value = target.value;
        this.save(value);
    }

    render() {
        return (
            <div className="selected-check-property">
                {this.props.editing ?
                    <div className="form-inline">
                        <label>Notification will be sent </label>&nbsp;
                        <div className="input-group">
                            <select name="delay" ref={(select) => {
                                select && select.focus()
                            }}
                                    className="form-control form-control-sm"
                                    style={{maxWidth: '10em'}}
                                    value={this.props.check.notificationDelay}
                                    onChange={this.onChange}
                                    disabled={this.state.processing}>
                                {this.props.options.map((option, index) =>
                                    <option label={formatDelay(option)}
                                            key={option}>{option}</option>
                                )}
                            </select>
                        </div>
                    </div>
                    :
                    <div><LabelDelay edit={this.props.edit}
                                     delay={this.props.check.notificationDelay}/>
                    </div>
                }
            </div>);
    }
}