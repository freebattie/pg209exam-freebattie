
import './App.css';

import {FrontPage} from "./frontpage";
import {UserPage} from "./userpage";
import {HashRouter, Route, Router, Routes} from "react-router-dom";
import {createHashHistory} from 'history';
import {useEffect, useState} from "react";

const history = createHashHistory();

function EditUser() {
    return <h1>TEST</h1>
}

function App() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeUserId, setActiveUserId] = useState(0);
    const [activeUserName, setActiveUserName] = useState("");
    useEffect(async () => {
        const res = await fetch("/api/users");

        setUsers(await res.json());
        setLoading(false);
    }, []);

    if (loading) {
        return (
            <div>Loading...</div>
        )
    }
    const setUserTo = (e)=>{
        setActiveUserId(e.target.value)
        setActiveUserName(e.target.options[e.target.value].text)
    }
    return (
        <div className="App">

            <Router basename="/" history={history} location={"/"}></Router>
            <HashRouter>
                <Routes>
                    <Route path={"/"} element={<FrontPage users={users} setUserTo={setUserTo}/> }/>
                    <Route path={"/user"} element={<UserPage activeUserId={activeUserId} activeUserName={activeUserName}/>}/>
                    <Route path={"/edituser"} element={<EditUser />}/>
                </Routes>
            </HashRouter>


        </div>

    )

}

export default App
