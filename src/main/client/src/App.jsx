import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Router, Routes, useNavigate} from "react-router-dom";
import {createHashHistory} from 'history';

const history = createHashHistory();
import imgUrl from './static/Logo.png'
import {NavBar} from "./Navbar";
import {FrontPage} from "./fronpage.jsx";





function ChatList({chats}) {
    console.log("this is empty",);
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

function UserPage({activeUserId}) {
    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    useEffect(async () => {
        const res = await fetch("/api/chats/"+activeUserId);

        setChats(await res.json());

        setLoading(false);



    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }
    console.log(chats)
    return(

        <div>
            <NavBar activeUserId={activeUserId}/>
            <div className="flex-container">

                <ChatList chats={chats}/>
                <Chat/>
            </div>
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
    const [activeUserId, setActiveUserId] = useState([]);
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
        setActiveUserId(user)
    }

    return (
        <div className="App">
            <Router basename="/" history={history} location={"/"}></Router>
            <HashRouter>
                <Routes>
                    <Route path={"/user"} element={<UserPage activeUserId={activeUserId}/>}/>
                    <Route path={"/*"} element={<FrontPage getActiveUser={getActiveUser} users={users}/>}/>

                </Routes>
            </HashRouter>
        </div>
    )

}

export default App
