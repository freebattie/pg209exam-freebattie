
import './App.css'
import {HashRouter, Route, Router, Routes} from "react-router-dom";
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

function Chat() {
    return <div class="flex-chat">
        <h1>PEr: hei</h1>
        <h1>Ppl: neei</h1>

    </div>
}

function ChatList() {
    return (
        <div class ="flex-chats">
            <ul>
                <li>PER</li>
                <li>PÅL</li>
                <li>Kari</li>
            </ul>

        </div>
    );
}

function UserPage(){

    return(
        <div>
            <NavBar/>
            <div class="flex-container">
            <ChatList/>
            <Chat/>
            </div>
        </div>
    );
}

function App() {

    return (
        <div className="App">
            <Router basename="/" history={history} location={"/"}></Router>
            <HashRouter>
                <Routes>
                    <Route path={"/*"} element={<FrontPage/>}/>
                    <Route path={"/user"} element={<UserPage/>}/>
                </Routes>
            </HashRouter>
        </div>
    )

}

export default App
