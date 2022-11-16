
import {useEffect, useState} from "react";
import {ChatList} from "./chatlist.jsx";
import {createHashHistory} from 'history';
import {Chat} from "./chat";
import { useNavigate} from "react-router-dom";

const history = createHashHistory();

export function UserPage({activeUserId,setActiveChat}) {
    const [activeUserName, setActiveUserName] = useState("");
    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [chat, setChat] = useState([]);
    const navigate = useNavigate()
    const [messages, setMessages] = useState([]);
    const getActiveChat =  (activChat)=>{
        setChat(activChat)
        setActiveChat(activChat)

        navigate("/user")

    }
    function handelNavigate(path) {

        history.push(path);
        window.location.reload(false);
    }
    useEffect( () => {
        const test = (async ()=>{
            const res = await fetch("/api/chats/"+activeUserId);

            setChats(await res.json());
            const userres = await fetch("/api/users/"+ activeUserId);
            const user = await userres.json();
            setActiveUserName(user.username);
            setLoading(false);
        })();

    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }

    function handelNavigateEditUser(s) {
        navigate(s);
    }

    if (chat.length == 0){
        return  <div>
            <div className="flex-container">


                <h1>{activeUserName}</h1>
                <ChatList className={"flex-chats" } chats={chats}  getActiveChat={getActiveChat}/>

                <h1 className={"flex-chat"}>Plese select a chat</h1>

                <button className={"button"} onClick={()=>handelNavigate('/')}>Back to users2</button>
                <button className={"button"} onClick={()=>handelNavigateEditUser('/edituser')}>Edit user</button>
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
                <button className={"button"} onClick={()=>handelNavigateEditUser('/edituser')}>Edit user</button>
                <button className={"button"} onClick={()=>handelNavigate('/newmessage')}>New Message</button>
            </div>
        </div>
    }

}