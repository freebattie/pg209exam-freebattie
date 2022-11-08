import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Router, Routes, useNavigate} from "react-router-dom";
import {createHashHistory} from 'history';
import {ListAllUsers} from "./ListAllUsers";
import {NavBar} from "./Navbar.jsx";


const history = createHashHistory();



function FrontPage() {

    return (

            <div>
                <ListAllUsers/>
            </div>


    )
}



function App() {

    return (
        <div className="App">
            <Router basename="/" history={history} location={"/"}></Router>
            <HashRouter>
                <Routes>
                    <Route path={"/*"} element={<FrontPage/>}/>
                    <Route path={"/shop"} element={<ListAllUsers/>}/>
                </Routes>
            </HashRouter>
        </div>
    )

}

export default App
