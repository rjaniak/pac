import React, {Component} from 'react'
import axios from "axios";
import Config from "../config";
import {Redirect} from "react-router-dom";

class RenameTalk extends Component {
    constructor(props) {
        super(props);

        let talk = ''
        let title = ''
        if (this.props.location.state && this.props.location.state.talk) {
            talk = this.props.location.state.talk
            title = talk.title
        }
        this.state = {
            talk: talk,
            newTitle: title,
            showSuccessMessage: false,
            showErrorMessage: false
        }

        this._handleChange = this._handleChange.bind(this);
        this._handleSubmit = this._handleSubmit.bind(this);
    }

    render() {
        if (!this.state.talk) {
            return <Redirect to='/'/>
        }

        let message = ''
        if (this.state.showSuccessMessage) {
            message = <div className='alert alert-success'>Update was successful!</div>
        } else if (this.state.showErrorMessage) {
            message = <div className='alert alert-danger'>Update failed, please try again later.</div>
        }

        return (
            <div className="update-talk-name">
                {message}
                <p>Here you can edit the talk name:</p>
                <form onSubmit={this._handleSubmit}>
                    <label>
                        New talk name:
                        <input type="text" value={this.state.newTitle} onChange={this._handleChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
            </div>
        )
    }

    _handleChange = event => {
        this.setState({
            newTitle: event.target.value
        });
    }

    _handleSubmit(event) {
        event.preventDefault();

        let token = this.props.keycloakState.keycloak.token

        let talk = this.state.talk
        talk.title = this.state.newTitle
        talk.eventId = talk.event.eventId
        talk.roomId = talk.room.roomId
        talk.personIds = talk.persons.map(function (person) {
            return person.personId
        })
        talk.topicIds = talk.topics.map(function (topic) {
            return topic.topicId
        })

        axios.put(Config.BACKEND_BASE_URL + '/talks/' + talk.talkId,
            talk,
            {
                headers: {
                    'Authorization': 'bearer ' + token
                }
            })
            .then(data => {
                this.setState({
                    showSuccessMessage: true,
                    showErrorMessage: false
                })
            })
            .catch(error => {
                this.setState({
                    showSuccessMessage: false,
                    showErrorMessage: true
                })
            })
    }
};

export default RenameTalk