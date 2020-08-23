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
                        <li className='h5' key={person.id}>
                            <span className='link person' onClick={() => this._handlePersonClick(person)}>{person.name}</span>
                            ({person.organization.name})
                        </li>
                    ))}
                </ul>
            </div>
        )
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

export default Persons