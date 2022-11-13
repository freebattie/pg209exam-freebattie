import {useEffect, useState} from "react";

export function Chat({chat, activeUserId}) {
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(false);
    useEffect(async () => {
        const res = await fetch("/api/messages/"+  new URLSearchParams({
            idChat: 1,
            idUser: activeUserId
        }));

        setMessages(await res.json());

        setLoading(false);



    }, []);
    console.log("Why u empty "+chat.id_chat)
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
