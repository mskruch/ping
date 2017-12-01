import React, {Component} from "react";
import Check from "./checks/check";
import AddCheck from "./checks/check-add";

export default class Checks extends Component {
    render() {
        let checks = this.props.checks.map((check, i) =>
            <Check key={check.id} number={i + 1} {...check}
                   deleteCheck={this.props.deleteCheck}
                   updateCheck={this.props.updateCheck}
                   outages={check.id === this.props.outagesCheckId}
                   toggleOutages={this.props.toggleOutages}
                   admin={this.props.admin}
                   select={this.props.select}
                   selected={this.props.selected && this.props.selected.id === check.id}
                   check={check}/>
        );

        return (
            <div className="container">
                {checks}
                <AddCheck addCheck={this.props.addCheck}/>
            </div>);
    }
}
