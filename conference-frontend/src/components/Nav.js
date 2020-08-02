import React, {Component} from 'react'
import {BrowserRouter as Router, NavLink, Route, Switch, Redirect} from "react-router-dom";
import Home from "./Home";
import Events from "./Events";
import Persons from "./Persons";

class Nav extends Component {
    render() {
        return (
            <Router>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
                        <div className="navbar-nav">
                            <NavLink className="nav-item nav-link" exact to="/">Home</NavLink>
                            <NavLink className="nav-item nav-link" to="/events">Events</NavLink>
                            <NavLink className="nav-item nav-link" to="/persons">Persons</NavLink>
                        </div>
                    </div>
                </nav>

                <div className="main-content">
                    <Switch>
                        <Route exact path="/">
                            <Home />
                        </Route>
                        <Route path="/events">
                            <Events />
                        </Route>
                        <Route path="/persons">
                            <Persons />
                        </Route>
                        <Redirect to="/" />
                    </Switch>
                </div>
            </Router>
        )
    }
};

export default Nav