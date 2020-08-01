import React, {Component} from 'react'
import axios from "axios";
import Config from "../config";

class Events extends Component {
    state = {
        events: []
    }

    componentDidMount() {
        axios
            .get(Config.BACKEND_BASE_URL + '/events')
            .then(({ data }) => {
                this.setState({
                    events: data
                })
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
                        <li key={event.id}>{event.name}</li>
                    ))}
                </ul>
            </div>
        )
    }
};

export default Events