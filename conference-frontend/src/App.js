import React, {Component} from 'react';
import './css/App.scss';
import 'bootstrap/dist/css/bootstrap.min.css';
import Footer from "./components/Footer";
import Header from "./components/Header";
import Nav from "./components/Nav";

class App extends Component {
    render() {
        return (
            <div className="conference-app">
                <Header/>
                <Nav/>
                <Footer/>
            </div>
        )
    }
}

export default App
