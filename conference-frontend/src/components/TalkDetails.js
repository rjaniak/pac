import React, {Component} from 'react'
import {Redirect} from "react-router-dom";
import axios from "axios";
import Config from "../config";

class TalkDetails extends Component {
    state = {
        talk: ''
    }

    componentDidMount() {
        axios
            .get(Config.BACKEND_BASE_URL + '/talks/' + this.props.location.state.talk.talkId)
            .then(({ data }) => {
                this.setState({
                    talk: data
                })
            })
            .catch(error => {
                this.setState({ talk: '' })
            })
    }

    render() {
        let talk = this.state.talk
        if (!talk) {
            if (!this.props.location.state || !this.props.location.state.talk) {
                return <Redirect to='/'/>
            }
            talk = this.props.location.state.talk
        }

        // Displa edit button if user is authenticated
        let keycloakState = this.props.keycloakState
        let editButton = ''
        if (keycloakState && keycloakState.authenticated) {
            editButton =
                <span className='link edit'  onClick={() => this._handleEditClick(talk)}>Edit</span>
        }

        return (
            <div>
                <h5>
                    <span className='link back' onClick={() => this._handleBackClick()}>Back</span>
                    <span>{talk.title}</span>
                    {editButton}
                </h5>
                <ul>
                    {this._getEventElement(talk)}
                    <li>Level: {talk.level}</li>
                    <li>Language: {talk.language}</li>
                    {this._getPersonList(talk)}
                    {this._getTopicList(talk)}
                </ul>
            </div>
        )
    }

    _getEventElement(talk) {
        if (!talk.event) {
            return ''
        } else {
            return (
                <li>Event: {talk.event.name}</li>
            )
        }
    }

    _getPersonList(talk) {
        if (!talk.persons) {
            return ''
        } else {
            return (
                <li>Persons:
                    <ul>
                        {talk.persons.map((person) => (
                            <li key={person.id}>
                                <span className='link person' onClick={() => this._handlePersonClick(person)}>{person.name}</span>
                            </li>
                        ))}
                    </ul>
                </li>
            )
        }
    }

    _getTopicList(talk) {
        if (!talk.topics) {
            return ''
        } else {
            return (
                <li>Topics:
                    <ul>
                        {talk.topics.map((topic) => (
                            <li key={topic.id}>
                                {topic.name}
                            </li>
                        ))}
                    </ul>
                </li>
            )
        }
}

    _handleBackClick = () => {
        this.props.history.goBack()
    }

    _handleEditClick = (talk) => {
        this.props.history.push({
            pathname: '/talks/edit',
            state: {
                talk: talk
            }
        });
    }

    _handlePersonClick = (person) => {
        this.props.history.push({
            pathname: '/persons/details',
            state: {
                person: person
            }
        });
    }
};

export default TalkDetails