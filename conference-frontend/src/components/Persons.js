import React, {Component} from 'react'
import axios from "axios";
import Config from "../config";

class Persons extends Component {
    state = {
        persons: []
    }

    componentDidMount() {
        axios
            .get(Config.BACKEND_BASE_URL + '/persons')
            .then(({ data }) => {
                this.setState({
                    persons: data
                })
            })
            .catch(error => {
                this.setState({ persons: [] })
            })
    }

    render() {
        return (
            <div>
                <ul>
                    {this.state.persons.map((person) => (
                        <li key={person.id}>{person.name}</li>
                    ))}
                </ul>
            </div>
        )
    }
};

export default Persons