
import {useEffect, useState} from "react";
import {ChatList} from "./chatlist.jsx";
import {createHashHistory} from 'history';
import {Chat} from "./chat";
import {Router, useNavigate} from "react-router-dom";

const history = createHashHistory();

export function UserPage({activeUserId, activeUserName,setActiveChat}) {
    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [chat, setChat] = useState([]);
    const navigate = useNavigate()
    const [messages, setMessages] = useState([]);
    const getActiveChat = async (activChat)=>{
        setChat(activChat)
        setActiveChat(activChat)
        const res = await fetch("/api/messages?"+  new URLSearchParams({
            idChat: activChat.id_chat,
            idUser: activeUserId
        }));

        setMessages(await res.json());

        setLoading(false);
        navigate("/user")
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
    if (chat.length == 0){
        return  <div>
            <div className="flex-container">


                <h1>{activeUserName}</h1>
                <ChatList className={"flex-chats" } chats={chats}  getActiveChat={getActiveChat}/>

                <h1 className={"flex-chat"}>Plese select a chat</h1>

                <button className={"button"} onClick={()=>handelNavigate('/')}>Back to users2</button>
                <button className={"button"} onClick={()=>handelNavigate('/edituser')}>Edit user</button>
                <button className={"button"} onClick={()=>handelNavigate('/newmessage')}>New Message</button>
            </div>
        </div>
    }
    else{
        return  <div>
            <div className="flex-container">


                <h1>{activeUserName}</h1>
                <ChatList className={"flex-chats" } chats={chats}  getActiveChat={getActiveChat}/>
                <Chat chat={chat} activeUserId={activeUserId} messages={messages} setMessages={setMessages}/>
                <button className={"button"} onClick={()=>handelNavigate('/')}>Back to users2</button>
                <button className={"button"} onClick={()=>handelNavigate('/edituser')}>Edit user</button>
                <button className={"button"} onClick={()=>handelNavigate('/newmessage')}>New Message</button>
            </div>
        </div>
    }

}