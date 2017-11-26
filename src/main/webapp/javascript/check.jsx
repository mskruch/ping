import React, {Component} from "react";

export default class CheckPage extends Component {
    back = () => {
        this.props.select(null);
    }

    render() {
        const {name, url} = {...this.props.check};

        return (
            <div className="container">
                <h1>{name || url}</h1>
                <div>
                    <button className="btn btn-info" onClick={this.back}>Back
                    </button>
                </div>
            </div>
        );
    }
}