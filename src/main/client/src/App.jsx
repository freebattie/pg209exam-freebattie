import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Router, Routes, useNavigate} from "react-router-dom";
import {createHashHistory} from 'history';

const history = createHashHistory();

import {NavBar} from "./Navbar";
import {FrontPage} from "./fronpage.jsx";





function ChatList({chats}) {

    return (
        <div className ="flex-chats">
            <ul>
                {chats.map((option, index) => {
                    return <li className="button"  key={index}>
                        {option.title}
                    </li>
                })}

            </ul>

        </div>
    );
}

function EditUser({activeUser}) {
    return <h1>DID WE GET HERE</h1>
}



function UserPage({activeUser}) {
    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    console.log(activeUser)
    useEffect(async () => {
        const res = await fetch("/api/chats/"+activeUser.value);

        setChats(await res.json());

        setLoading(false);



    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }

    return(

        <div>
            <NavBar activeUser={activeUser}/>
                <Routes>
                    <Route path={"/*"} element={
                        <div>
                            <div className="flex-container">
                                <ChatList chats={chats}/>
                                <Chat/>
                            </div>
                        </div>
                    }/>
                    <Route path={"/edituser"} element={
                        <div>

                            <div className="flex-container">
                                <EditUser activeUser={activeUser}/>
                                <Chat/>
                            </div>
                        </div>
                    }/>

                </Routes>

        </div>
    );
}
function Chat() {
    return <div className="flex-chat">
        <div className="chat-card">
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <h1>PEr: hei</h1>
            <h1>Ppl: neei</h1>
            <div className="bottom">
                <label>Text: <input className="input"/></label>
                <button className="buttonSend">Send</button>
            </div>
        </div>



    </div>
}
function App() {

    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeUser, setActiveUser] = useState([]);




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
    function getActiveUser(user){
        setActiveUser(user)
    }

    return (
        <div className="App">
            <Router basename="/" history={history} location={"/"}></Router>
            <HashRouter>
                <Routes>
                    <Route path={"/user"} element={<UserPage activeUser={activeUser}/>}/>
                    <Route path={"/*"} element={<FrontPage getActiveUser={getActiveUser} users={users}/>}/>

                </Routes>
            </HashRouter>
        </div>
    )

}

export default App
