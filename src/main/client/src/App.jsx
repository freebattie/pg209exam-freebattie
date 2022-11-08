import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Router, Routes, useNavigate} from "react-router-dom";
import {createHashHistory} from 'history';

const history = createHashHistory();
import imgUrl from './static/Logo.png'












 function FrontPage({users,getActiveUser}) {




     return (
         <div>

             <center>

                 <img src={imgUrl} alt="Social Buzz Logo!" width="600" height="300"/>

                 <div>

                 </div>

                 <select onChange={(e)=>getActiveUser(e.target.value)}>

                     <option>Please choose one option</option>
                     {users.map((option, index) => {
                         return <option value={option.id_user} key={index}>
                             {option.username}
                         </option>
                     })}
                 </select>
                 <br/>
                 <button><Link to={"/user"}>Show all items</Link></button>
             </center>
         </div>

     );

}


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

function UserPage({activeUser}) {
    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    useEffect(async () => {
        const res = await fetch("/api/chats/"+activeUser);

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
    const [activeUser, setActiveUser] = useState([]);



    useEffect(async () => {
        const res = await fetch("/api/user-login");

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
