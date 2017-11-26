import React, {Component} from "react";
import Check from "./checks/check";
import AddCheck from "./checks/check-add";

export default class Checks extends Component {
    render() {
        var checks = this.props.checks.map((check, i) =>
            <Check key={check.id} number={i + 1} {...check}
                   deleteCheck={this.props.deleteCheck}
                   updateCheck={this.props.updateCheck}
                   outages={check.id === this.props.outagesCheckId}
                   toggleOutages={this.props.toggleOutages}/>
        );

        return (
            <table className="table table-hover">
                <thead>
                <tr>
                    <th scope="col action" width="0%">#</th>
                    <th scope="col" width="20%">Name</th>
                    <th scope="col" width="30%">Url</th>
                    <th scope="col" width="10%">Delay</th>
                    <th scope="col" width="20%">Status</th>
                    <th scope="col action" width="20%"></th>
                </tr>
                </thead>
                <tbody>
                {checks}
                <AddCheck addCheck={this.props.addCheck}/>
                </tbody>
            </table>
        );
    }
}
