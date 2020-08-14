import React, {Component} from 'react'
import axios from "axios";
import Config from "../config";

class RenameTalk extends Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange = event => {
        this.setState({
            value: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        let token = this.props.keycloakState.keycloak.token
        let talk = this.props.talk

        talk.name = this.state.value

        axios.post(Config.BACKEND_BASE_URL + '/talks/' + talk.talkId,
            talk,
            {
                headers: {
                    'Authorization': 'bearer ' + token
                }
            })
            .then(data => {
                console.log(data);
            })
    }

    render() {
        let talk = this.props.talk
        return (
            <div className="update-talk-name">
                <p>Here you can update the name of the existing talk "{talk.name}":</p>
                <form onSubmit={this.handleSubmit}>
                    <label>
                        New talk name:
                        <input type="text" value={this.state.value} onChange={this.handleChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
            </div>
        )
    }
};

export default RenameTalk