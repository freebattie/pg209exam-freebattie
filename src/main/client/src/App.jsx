
import './App.css';

import {FrontPage} from "./frontpage";
import {UserPage} from "./userpage";
import {HashRouter, Route, Router, Routes, useNavigate} from "react-router-dom";
import {createHashHistory} from 'history';
import {useEffect, useState} from "react";
import {EditUser} from "./edituser.jsx";


const history = createHashHistory();


function NewChat({users,activeUserId}) {
    const [title, setTitle] = useState("");
    const [chatters, setChatters] = useState([email]);

    async function handelOnSubmit(){
        await fetch("/api/chats", {
            method: "POST",
            body: JSON.stringify({title,chatters}),
            headers: {
                "Content-Type": "application/json"
            }
        });
    }

    return (
        <div>
            <h1>New message</h1>

            <form onSubmit={handelOnSubmit}>
                <div>
                    Title:<input value={name} type="text" onChange={(e) => setTitle(e.target.value)}/>
                </div>
                <select onChange={(e)=>setUserTo(e)}>

                    <option>Add user to chat</option>
                    {users.map((option, index) => {
                        return <option value={option.id_user} key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
                <br/>
                <button className={"button"} onClick={()=>handelNavigate()}></button>
                <br/>
                    <!--add users here ? -->
                <div>
                    {emails.map((email,index)=>{

                        return <div>
                            email {index+1} :<input  value={email.email} type="text"
                                                     onChange={(e) => handelSetEmail(e,index)}/>
                        </div>
                    })}
                    <button onClick={(e)=>handelAddMail(e)}>add mail</button><button onClick={(e)=>handelRemoveEmail(e)}>remove mail</button>
                </div>


                <button>Update</button>
            </form>

        </div>
    )
}



function App() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeUserId, setActiveUserId] = useState(0);
    const [activeChat, setActiveChat] = useState(0);
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
                    <Route path={"/user/*"} element={<UserPage setActiveChat={setActiveChat} activeUserId={activeUserId} activeUserName={activeUserName}/>}/>
                    <Route path={"/edituser"} element={<EditUser activeUserId={activeUserId}/>}/>
                    <Route path={"/newchat"} element={<NewChat users={users} activeUserId={activeUserId}/>}/>

                </Routes>
            </HashRouter>


        </div>

    )

}

export default App
