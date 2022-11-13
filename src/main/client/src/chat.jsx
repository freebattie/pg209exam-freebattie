import {useEffect, useState} from "react";

export function Chat({chat, activeUserId}) {
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(false);
    const [myUserChat, setMyUserChat] = useState(1);

    useEffect(async () => {
        const res = await fetch("/api/messages?"+  new URLSearchParams({
            idChat: myUserChat,
            idUser: activeUserId
        }));

        setMessages(await res.json());

        setLoading(false);



    }, [myUserChat]);
    console.log(messages)
    if (loading) {

        return (
            <div>Loading...</div>

        )
    }

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
