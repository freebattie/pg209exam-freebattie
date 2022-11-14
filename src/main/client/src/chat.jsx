import {useEffect, useRef, useState} from "react";
import {useNavigate} from "react-router-dom";

export function Chat({chat, activeUserId}) {
    const navigate = useNavigate();


    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(false);
    const [myUserChat, setMyUserChat] = useState("");
    const [myMessage, setMyMessage] = useState();
    const AlwaysScrollToBottom = () => {
        const elementRef = useRef();
        useEffect(() => elementRef.current.scrollIntoView());
        return <div ref={elementRef} />;
    };
    useEffect(async () => {
        const res = await fetch("/api/messages?"+  new URLSearchParams({
            idChat: chat.id_chat,
            idUser: activeUserId
        }));

        setMessages(await res.json());

        setLoading(false);



    }, [chat,myUserChat]);
    console.log(messages)
    if (loading) {

        return (
            <div>Loading...</div>

        )
    }

    async function handelOnSubmit(e) {
        e.preventDefault();
        console.log("chat is" +chat)
        await fetch("/api/messages", {
            method: "POST",
            body: JSON.stringify({message:myMessage,idChat:chat.id_chat,user:{id_user:activeUserId}}),
            headers: {
                "Content-Type": "application/json"
            }
        });
        setMyMessage("");
        setLoading(true)
        const res = await fetch("/api/messages?"+  new URLSearchParams({
            idChat: chat.id_chat,
            idUser: activeUserId
        }));

        setMessages(await res.json());

        setLoading(false);
        window.scrollTo({
            top: document.documentElement.scrollHeight,
            behavior: 'smooth',
        });
        //navigate("/user");
    }

    return <div className="flex-chat">
        <div  className="chat-card">
            {messages.map((option, index) => {
                return <div>
                    <h1  key={index}>
                        {option.idMessage} @{option.timestamp}
                    </h1>
                    <h2>{option.message}</h2>
                </div>
            })}
            <AlwaysScrollToBottom />
            <div className="bottom">
                <form onSubmit={(e)=>handelOnSubmit(e)}>
                    <label>Text: <input value={myMessage} type="text"
                                        onChange={(e) => setMyMessage(e.target.value)}/></label>
                    <button className="buttonSend">Send</button>
                </form>

            </div>
        </div>



    </div>
}
