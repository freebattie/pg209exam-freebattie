
import {useEffect, useState} from "react";
import {ChatList} from "./chatlist.jsx";
import {createHashHistory} from 'history';
import {Chat} from "./chat";
import { useNavigate} from "react-router-dom";
import imgUrl from "./static/Logo.png";

const history = createHashHistory();

export function UserPage({activeUserId,setActiveChat,activeUserName}) {

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

            <div className={"user"}>

                <button className={"button"} onClick={()=>handelNavigate('/')}>Home</button>
                <button className={"buttonUser"} onClick={()=>handelNavigateEditUser('/edituser')}>Edit user: {activeUserName}</button>
            </div>
            <div className="flex-container">
                <ChatList className={"flex-chats" } chats={chats}  getActiveChat={getActiveChat}/>
                <h1 className={"flex-chat"}>Plese select a chat</h1>
            </div>
        </div>
    }
    else{
        return  <div>
            <div className={"user"}>

                <button className={"button"} onClick={()=>handelNavigate('/')}>Home</button>
                <button className={"buttonUser"} onClick={()=>handelNavigateEditUser('/edituser')}>Edit user: {activeUserName}</button>
            </div>

                <div className="flex-container">
                    <ChatList className={"flex-chats" } chats={chats}  getActiveChat={getActiveChat}/>
                    <Chat chat={chat} activeUserId={activeUserId} messages={messages} setMessages={setMessages}/>
                </div>


        </div>
    }

}