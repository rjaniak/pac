import React, {Component} from 'react'
import logo from '../images/prodyna-logo.png'

class Header extends Component {
    render() {
        return (
            <div className="header row">
                <div className="col-6">
                    <h1 className="headline">Conference App</h1>
                </div>
                <div className="col-6">
                    <img className="logo" src={logo} />
                </div>
            </div>
        )
    }
};

export default Header