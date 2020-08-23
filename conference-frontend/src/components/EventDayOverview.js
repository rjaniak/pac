import React, {Component} from 'react'
import {Redirect} from "react-router-dom";
import moment from 'moment';
import axios from "axios";
import Config from "../config";

class EventDayOverview extends Component {
    constructor(props) {
        super(props);

        let event = ''
        if (this.props.location.state) {
            event = this.props.location.state.event
        }

        this.state = {
            event: event,
            talks: [],
            currentDay: ''
        }

        this._handleChange = this._handleChange.bind(this);
    }

    componentDidMount() {
        axios
            .get(Config.BACKEND_BASE_URL + '/events/' + this.state.event.eventId + '/talks')
            .then(({ data }) => {
                this.setState({
                    talks: data
                })
            })
            .catch(error => {
                this.setState({ talks: [] })
            })
    }

    render() {
        // Get selected event
        let event = this.state.event
        if (!event) {
            return <Redirect to='/events'/>
        }

        // Create days list for select input field
        let begin = new Date(event.begin)
        let end = new Date(event.end)
        // Calculate day difference and add 1 (if 1 day difference between start and end, there a 2 event days)
        const secondsPerDay = 86400000
        let days = Math.floor((end - begin) / secondsPerDay) + 1
        let dayList = []
        for (let i = 0; i < days; i++) {
            let nextDay = new Date(begin.getTime() + i * secondsPerDay);
            dayList.push(nextDay.toISOString().split('T')[0])
        }

        // Get selected day
        let currentDay = this.state.currentDay
        if (!currentDay) {
            // Use first event day as default
            currentDay = begin.toISOString().split('T')[0]
        }

        return (
            <div>
                <h5>
                    <span className='link back' onClick={() => this._handleBackClick()}>Back</span>
                    <span>Day overview of {event.name}</span>
                </h5>
                <span>Select day:</span>
                <select onChange={this._handleChange} value={currentDay}>
                    {dayList.map(function(nextDay){
                        return (
                            <option value={nextDay} key={nextDay}>
                                {new Date(nextDay).toISOString().split('T')[0]}
                            </option>
                        )
                    })}
                </select>
                {this._getEventTable(currentDay)}
            </div>
        )
    }

    _getEventTable(currentDay) {
        let instance = this
        let eventTalks = this.state.talks
        let talks = []
        let rooms = []

        // Currently only time slots on hourly base supported in this view (e.g. 12:00 - 14:00)
        // Get start and end hours to create table on hourly base
        let dayStartHour = 24
        let dayEndHour = 0

        eventTalks.forEach(eventTalk => {
            // only use talks from current day
            if (eventTalk.date === currentDay) {
                talks.push(eventTalk)
                if (rooms.filter(room => room.roomId === eventTalk.room.roomId).length === 0) {
                    rooms.push(eventTalk.room)
                }

                let start = moment(eventTalk.startTime, 'HH:mm')
                let startHour = start.hours()
                let endHour = start.add(eventTalk.duration, 'minutes').hours()
                if (startHour < dayStartHour) {
                    dayStartHour = startHour
                }
                if (endHour > dayEndHour) {
                    dayEndHour = endHour
                }
            }
        })

        // Order rooms by name
        rooms.sort((a, b) => (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0))

        // Each row is 1 hour time slot
        const rows = []
        for (let i = dayStartHour; i < dayEndHour; i++) {
            rows.push(
                <tr key={i}>
                    <td>{i}:00 - {i + 1}:00</td>
                    {rooms.map(function(room){
                        return (
                            <td key={room.roomId}>
                                {instance._getTalkForRoomAndTimeSlot(talks, room, i)}
                            </td>
                        )
                    })}
                </tr>
            )
        }

        return (
            <table className="table table-bordered">
                <thead>
                <tr>
                    <th scope="col">Time slot</th>
                    {rooms.map(function(room){
                        return (
                            <th scope="col" key={room.roomId}>{room.name}</th>
                        )
                    })}
                </tr>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </table>
        )
    }

    _getTalkForRoomAndTimeSlot(talks, room, timeSlotStartHour) {
        // Check if there is a talk in that room for this time slot
        let talk
        for (let i = 0; i < talks.length; i++) {
            let nextTalk = talks[i]
            // Check room
            if (nextTalk.room.roomId !== room.roomId) {
                continue
            }
            // Check time slot
            let talkStartTime = moment(nextTalk.startTime, 'HH:mm')
            let talkStartHour = talkStartTime.hours()
            let talkEndHour = talkStartTime.add(nextTalk.duration, 'minutes').hours()
            if (talkEndHour <= timeSlotStartHour || talkStartHour > timeSlotStartHour) {
                continue
            }
            // This talk takes place in current room and current time slot
            talk = nextTalk
        }

        // Create output
        if (talk) {
            // Create topics list
            let topics = ''
            if (talk.topics) {
                let topicList = talk.topics.map(function (topic) {
                    return topic.name
                })
                topics = topicList.join(', ')
            }

            // Create persons list
            let persons = ''
            if (talk.persons) {
                let personList = talk.persons.map(function (person) {
                    return person.name
                })
                persons = personList.join(', ')
            }

            return (
                <div className="talk-details">
                    <div className="h6">
                        <span className="link" onClick={() => this._handleTalkClick(talk)}>
                            <strong>{talk.title}</strong>
                        </span>
                    </div>
                    <div><strong>Topics:</strong> {topics}</div>
                    <div><strong>Persons:</strong> {persons}</div>
                    <div><strong>Level:</strong> {talk.level}</div>
                </div>
            )
        }
    }

    _handleBackClick = () => {
        this.props.history.goBack()
    }

    _handleChange = event => {
        this.setState({
            currentDay: event.target.value
        });
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

export default EventDayOverview