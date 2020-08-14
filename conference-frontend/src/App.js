import React, {Component} from 'react';
import './css/App.scss';
import 'bootstrap/dist/css/bootstrap.min.css';
import Config from "./config";
import Keycloak from "keycloak-js";
import Footer from "./components/Footer";
import Header from "./components/Header";
import Main from "./components/Main";

class App extends Component {
    constructor(props) {
        super(props);

        const keycloak = Keycloak(Config.KEYCLOAK_INIT_OPTIONS);

        this.state = {
            initialized: false,
            keycloak: keycloak,
            authenticated: false
        }
    }

    componentDidMount() {
        this.state.keycloak.init({onLoad: 'check-sso'}).then(authenticated => {
            this.setState({
                initialized: true,
                authenticated: authenticated
            })
        })
    }

    render() {
        return (
            <div className="conference-app">
                <Header/>
                <Main keycloakState={this.state} />
                <Footer/>
            </div>
        )
    }
}

export default App
