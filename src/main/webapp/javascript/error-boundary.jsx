import React, {Component} from "react";

export default class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {hasError: false};
    }

    componentDidCatch(error, info) {
        this.setState({hasError: true});
        console.log(error, info);
    }

    render() {
        if (this.state.hasError) {
            return <div className="text-center"><span
                className="status badge badge-danger">Something went wrong</span>
            </div>;
        }
        return this.props.children;
    }
}
