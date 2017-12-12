import React, {Component} from "react";
import Body from "./body";
import Footer from "./footer"

export default class Check extends Component {
    state = {
        processing: false,
        editingName: false,
        editingDelay: false
    };

    delete = () => {
        this.setState({processing: true})
        fetch('/api/checks/' + this.props.check.id,
            {
                credentials: 'same-origin',
                method: 'delete'
            })
            .then((response) => {
                if (response.status >= 400) {
                    throw new Error('check not deleted');
                }
                this.setState({processing: false});
                this.props.deleteCheck(this.props.check.id);
            }).catch((error) => {
                console.error(error);
                this.setState({processing: false});
            }
        );
    }

    pause = () => {
        this.patchPaused(true);
    }

    resume = () => {
        this.patchPaused(false);
    }

    patchPaused = (value) => {
        this.setState({processing: true});
        fetch('/api/checks/' + this.props.check.id,
            {
                credentials: 'same-origin',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    "paused": value
                })
            })
            .then((response) => {
                return response.json();
            })
            .then((responseData) => {
                this.props.updateCheck(responseData);
                this.setState({processing: false});
            });
    }

    clicked = (event) => {
        if (!this.props.selected) {
            this.props.select(this.props.check);
        } else {
            if (this.state.editingName && event.target.tagName.toUpperCase() != 'INPUT') {
                this.editName(false);
            }
            if (this.state.editingDelay && event.target.tagName.toUpperCase() != 'SELECT') {
                this.editDelay(false);
            }
        }
    }

    editName = (value = true) => {
        this.setState({editingName: value});
    }

    editDelay = (value = true) => {
        this.setState({editingDelay: value});
    }

    componentWillReceiveProps(nextProps) {
        if (!nextProps.selected) {
            this.setState({editingName: false, editingDelay: false});
        }
    }

    render() {
        let className = "card mb-3 check"
            + (this.props.selected ? "-selected" : "")
            + (this.props.check.paused ? " text-muted" : "");
        return (
            <div onClick={this.clicked}
                 style={this.props.selected ? {} : {cursor: 'pointer'}}
                 className={className}>
                <Body check={this.props.check}
                      status={this.props.check.status}
                      updateCheck={this.props.updateCheck}
                      selected={this.props.selected}
                      delete={this.delete}
                      pause={this.pause}
                      resume={this.resume}
                      processing={this.state.processing}
                      editingName={this.state.editingName}
                      editName={this.editName}
                      editingDelay={this.state.editingDelay}
                      editDelay={this.editDelay}
                      fetched={this.props.fetched}/>
                <Footer render={this.props.selected}
                        checkId={this.props.check.id}
                        fetched={this.props.fetched}/>
            </div>
        );

    }
}
