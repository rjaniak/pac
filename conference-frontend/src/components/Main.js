import React, {Component} from 'react'
import {BrowserRouter as Router, NavLink, Route, Switch, Redirect} from "react-router-dom";
import Home from "./Home";
import Events from "./Events";
import Persons from "./Persons";
import PersonDetails from "./PersonDetails";
import TalkDetails from "./TalkDetails";
import RenameTalk from "./RenameTalk";
import EventDayOverview from "./EventDayOverview";

class Main extends Component {
    render() {
        let keycloakState = this.props.keycloakState
        let loginNavButton
        if (!keycloakState.initialized) {
            loginNavButton = ''
        } else if (keycloakState.authenticated) {
            loginNavButton = <button className="nav-item nav-link" onClick={() => this._logout()}>Logout</button>
        } else {
            loginNavButton = <button className="nav-item nav-link" onClick={() => this._login()}>Login</button>
        }

        let mainContent
        if (keycloakState.initialized) {
            mainContent =
                <div className="main-content">
                    <Switch>
                        <Route exact path="/">
                            <Home keycloakState={keycloakState} />
                        </Route>
                        <Route exact path="/events" component={Events} />
                        <Route exact path="/events/details" component={EventDayOverview} />
                        <Route exact path="/persons" component={Persons} />
                        <Route exact path="/persons/details" component={PersonDetails} />
                        <Route exact path="/talks/details" render={(props) =>
                            <TalkDetails {...props} keycloakState={keycloakState} />
                        } />
                        <Route exact path="/talks/edit" render={(props) =>
                            <RenameTalk {...props} keycloakState={keycloakState} />
                        } />
                        <Redirect to="/" />
                    </Switch>
                </div>
        } else {
            mainContent =
                <div className="main-content">
                    Loading...
                </div>
        }

        return (
            <Router>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
                        <div className="navbar-nav">
                            <NavLink className="nav-item nav-link" exact to="/">Home</NavLink>
                            <NavLink className="nav-item nav-link" to="/events">Events</NavLink>
                            <NavLink className="nav-item nav-link" to="/persons">Persons</NavLink>
                            {loginNavButton}
                        </div>
                    </div>
                </nav>

                {mainContent}
            </Router>
        )
    }

    _login = () => {
        let redirect = window.location.protocol + '//' + window.location.host + '/';
        this.props.keycloakState.keycloak.login({redirectUri: redirect})
    }

    _logout = () => {
        let redirect = window.location.protocol + '//' + window.location.host + '/';
        this.props.keycloakState.keycloak.logout({redirectUri: redirect})
    }
};

export default Main