import React, {Component} from 'react'
import {Redirect} from "react-router-dom";
import axios from "axios";
import Config from "../config";

class PersonDetails extends Component {
    state = {
        person: ''
    }

    componentDidMount() {
        let person = this.props.location.state.person
        axios
            .get(Config.BACKEND_BASE_URL + '/persons/' + person.personId)
            .then(({ data }) => {
                this.setState({
                    person: data
                })
            })
            .catch(error => {
                this.setState({ persons: person })
            })
    }

    render() {
        let person = this.state.person
        if (!person) {
            if (!this.props.location.state || !this.props.location.state.person) {
                return <Redirect to='/persons'/>
            }
            person = this.props.location.state.person
        }

        return (
            <div>
                <h5>
                    <span className='link back' onClick={() => this._handleBackClick()}>Back</span>
                    <span>Talks with {person.name}</span>
                </h5>
                {this._getTalkList(person)}
            </div>
        )
    }

    _getTalkList(person) {
        let talks = person.talks
        if (!talks) {
            return ''
        } else {
            // Order by talk title
            talks.sort((a, b) => (a.title > b.title) ? 1 : ((b.title > a.title) ? -1 : 0))
            return (
                <ul>
                    {talks.map((talk) => (
                        <li key={talk.id}>
                            <span className='link' onClick={() => this._handleTalkClick(talk)}>{talk.title}</span>
                        </li>
                    ))}
                </ul>
            )
        }
    }

    _handleBackClick = () => {
        this.props.history.goBack()
    }

    _handleTalkClick = (talk) => {
        this.props.history.push({
            pathname: '/talks/details',
            state: {
                talk: talk
            }
        })
    }
};

export default PersonDetails