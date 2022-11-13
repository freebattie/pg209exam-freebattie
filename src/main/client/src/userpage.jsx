
import {useEffect, useState} from "react";
import {ChatList} from "./chatlist.jsx";
import {createHashHistory} from 'history';
import {Chat} from "./chat";

const history = createHashHistory();

export function UserPage({activeUserId, activeUserName}) {
    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [chat, setChat] = useState([]);
    const getActiveChat =(activChat)=>{
        setChat(activChat)
        console.log("this chat "+activChat);
    }
    function handelNavigate(path) {

        history.push(path);
        window.location.reload(false);
    }
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
    return  <div>
        <div className="flex-container">
            <h1>{activeUserName}</h1>
            <ChatList className={"flex-chats" } chats={chats}  getActiveChat={getActiveChat}/>
            <Chat chat={chat} activeUserId={activeUserId}/>
            <button className={"button"} onClick={()=>handelNavigate('/')}>Back to users</button>
            <button className={"button"} onClick={()=>handelNavigate('/edituser')}>Edit user</button>
        </div>
    </div>
}