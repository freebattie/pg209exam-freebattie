import {useEffect, useRef, useState} from "react";


export function Chat({chat, activeUserId, messages,setMessages}) {

    const [loading, setLoading] = useState(false);
    const [users, setUsers] = useState([]);

    const [myMessage, setMyMessage] = useState();
    useEffect(() => {
        const test = async ()=>{
            const res = await fetch("/api/messages?"+  new URLSearchParams({
                idChat: chat.id_chat,
                idUser: activeUserId
            }));
            const Ures = await fetch("/api/chats/usernames/"+chat.id_chat)
            setUsers(await Ures.json());

            setMessages(await res.json());

            await fetch('/api/messages/update?idChat='+chat.id_chat+"&idUser="+activeUserId, {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json;'
                },
            })
            setLoading(false);
        }
        test();
    }, [chat.id_chat]);
    const AlwaysScrollToBottom = () => {
        const elementRef = useRef();
        useEffect(() => elementRef.current.scrollIntoView());
        return <div ref={elementRef} />;
    };



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
            body: JSON.stringify({message:myMessage,idChat:chat.id_chat,user:activeUserId}),
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

        <div >
        <div className="chat-card">
            {messages.map((message, index) => {
                return <div className={"chatContainer"}>

                        <div className={"chatHeader"} key={index}>
                            <div className={"chatName"}>{message.username}</div>
                            <div className={"timestamp"}>@{message.timestamp}</div>
                        </div>

                        <div className={"chatMessage"}>{message.message}</div>
                        <div className={"lastreadlist"}>{message.lastReads.size > 0 ?"last read:": "" } {message.lastReads.map((p)=>{
                            return <div className={"lastRead"}>{p.username} ~ {p.timestamp}</div>
                        })}</div>



                </div>
            })}
            <AlwaysScrollToBottom />
            <div className="bottom">
                <form onSubmit={(e)=>handelOnSubmit(e)}>


                    <label>Text: <input value={myMessage} type="text" className="write-message2" placeholder="Type your message here" onChange={(e) => setMyMessage(e.target.value)}>

                    </input>
                        <button className={"button"}>Send</button>
                    </label>

                </form>

            </div>
        </div>


        </div>

        <div className={"userList border"}> in chat: {users.map((user)=>{
                return <div className={"userInChat"}>{user}</div>
            }

        )}</div>
    </div>
}
