import React, {Component} from 'react'
import {BrowserRouter as Router, NavLink, Route, Switch, Redirect} from "react-router-dom";
import Home from "./Home";
import Events from "./Events";
import Persons from "./Persons";

class Main extends Component {
    login() {
        this.props.keycloakState.keycloak.login().then(authenticated => {
            return (
                <Redirect to="/" />
            )
        })
    }

    logout() {
        this.props.keycloakState.keycloak.logout()
    }

    render() {
        let keycloakState = this.props.keycloakState
        let loginNavButton
        if (!keycloakState.initialized) {
            loginNavButton = ''
        } else if (keycloakState.authenticated) {
            loginNavButton = <button className="nav-item nav-link" onClick={() => this.logout()}>Logout</button>
        } else {
            loginNavButton = <button className="nav-item nav-link" onClick={() => this.login()}>Login</button>
        }

        let mainContent
        if (keycloakState.initialized) {
            mainContent =
                <div className="main-content">
                    <Switch>
                        <Route exact path="/">
                            <Home keycloakState={keycloakState} />
                        </Route>
                        <Route path="/events">
                            <Events keycloakState={keycloakState} />
                        </Route>
                        <Route path="/persons">
                            <Persons keycloakState={keycloakState} />
                        </Route>
                        <Redirect to="/" />
                    </Switch>
            </div>
        } else {
            mainContent = <div className="main-content">
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
};

export default Main