import React, {Component} from "react";
import Check from "./checks/check";


export default class Checks extends Component {
    render() {
        let checks = this.props.ready ?
            this.props.checks.map((check, i) =>
                <Check key={check.id} number={i + 1} {...check}
                       deleteCheck={this.props.deleteCheck}
                       updateCheck={this.props.updateCheck}
                       admin={this.props.admin}
                       select={this.props.select}
                       selected={this.props.selected && this.props.selected.id === check.id}
                       check={check}/>
            ) :
            <div className="card mb-3">
                <div className="card-body text-center"><img
                    src="/images/ajax-loader.gif"/></div>
            </div>;

        return (
            <div className="container">
                {checks}
            </div>);
    }
}
