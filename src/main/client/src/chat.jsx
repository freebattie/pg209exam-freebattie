import {useEffect, useRef, useState} from "react";


export function Chat({chat, activeUserId, messages,setMessages}) {

    const [loading, setLoading] = useState(false);

    const [myMessage, setMyMessage] = useState();
    useEffect(() => {
        const test = async ()=>{
            const res = await fetch("/api/messages?"+  new URLSearchParams({
                idChat: chat.id_chat,
                idUser: activeUserId
            }));

            setMessages(await res.json());

            await fetch('/api/messages/update', {
                method: 'POST',
                body: JSON.stringify({
                    idChat: chat.id_chat,
                    idUser: activeUserId,

                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
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
            {messages.map((message, index) => {
                return <div>

                    <h1  key={index}>
                        {message.user.username} @{message.timestamp}


                    </h1>
                    <h3>last read: {message["lastReads"].map((p)=>{
                        return <h1>{p.username} - {p.timestamp}</h1>
                    })}</h3>
                    <h2>{message.message}</h2>
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
