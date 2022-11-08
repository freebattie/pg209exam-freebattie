
import './App.css'
import {HashRouter, Route, Router, Routes} from "react-router-dom";
import {createHashHistory} from 'history';
import {ListAllUsers} from "./ListAllUsers";
import {NavBar} from "./Navbar.jsx";
import {useEffect, useState} from "react";


const history = createHashHistory();



function FrontPage() {

    return (

            <div>
                <ListAllUsers/>
            </div>


    )
}

function Chat() {
    return <div class="flex-chat">
        <div class={"chat-card"}>
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
            <div className={"bottom"}>
                <label>Text: <input className={"input"}/></label>
                <button className={"buttonSend"}>Send</button>
            </div>
        </div>



    </div>
}

function ChatList() {
    return (
        <div class ="flex-chats">
            <ul>
                <li class={"button"}>New Chat</li>
                <li class={"button"}>PER</li>
                <li class={"button"}>PÅL</li>
                <li class={"button"}>Kari</li>
            </ul>

        </div>
    );
}

function UserPage({prop} ){
    const setChats = prop.setChats;
    const chats = prop.chats;
    const [loading, setLoading] = useState(true);
    useEffect(async () => {
        const res = await fetch("/api/chat-list");

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
            <NavBar/>
            <div class="flex-container">
            <ChatList chats={chats}/>
            <Chat/>
            </div>
        </div>
    );
}

function App() {
    const [chats, setChats] = useState({});


    return (
        <div className="App">
            <Router basename="/" history={history} location={"/"}></Router>
            <HashRouter>
                <Routes>
                    <Route path={"/*"} element={<FrontPage chats={chats}/>}/>
                    <Route path={"/user"} element={<UserPage setChats={setChats} chats={chats}/>}/>
                </Routes>
            </HashRouter>
        </div>
    )

}

export default App
