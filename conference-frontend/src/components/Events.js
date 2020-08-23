import React, {Component} from 'react'
import axios from "axios";
import Config from "../config";

class Events extends Component {
    constructor(props) {
        super(props);
        this.state = {
            events: []
        }
    }

    componentDidMount() {
        // Get event data
        axios
            .get(Config.BACKEND_BASE_URL + '/events')
            .then(({ data }) => {
                this._setEventsData(data)
            })
            .catch(error => {
                this.setState({ events: [] })
            })
    }

    render() {
        return (
            <div>
                <ul>
                    {this.state.events.map((event) => (
                        <li key={event.id}>
                            <span className='h5 link' onClick={() => this._handleEventClick(event)}>{event.name}</span>
                            <ul>
                                <li><strong>Date</strong>: {event.begin} - {event.end}</li>
                                <li><strong>Location</strong>: {event.location.name}</li>
                                <li><strong>Topics</strong>: {event.topicList}</li>
                            </ul>
                        </li>
                    ))}
                </ul>
            </div>
        )
    }

    _handleEventClick = (event) => {
        this.props.history.push({
            pathname: '/events/details',
            state: {
                event: event
            }
        })
    }

    _setEventsData(events) {
        // Also get topics for events
        for (let i = 0; i < events.length; i++) {
            axios
                .get(Config.BACKEND_BASE_URL + '/events/' + events[i].eventId + '/topics')
                .then(({ data }) => {
                    let topicList = data.map(function (topic) {
                        return topic.name
                    })
                    // Add topics to event in state
                    if (this.state.events) {
                        let updatedEvents = this.state.events
                        updatedEvents[i].topicList = topicList.join(', ')
                        this.setState({
                            events: updatedEvents
                        })
                    }
                })
                .catch(error => {
                    console.error('Cannot load topics for event id ' + events[i].eventId)
                })
        }

        this.setState({
            events: events
        })
    }
};

export default Events