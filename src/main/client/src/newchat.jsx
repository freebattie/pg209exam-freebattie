import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export function NewChat({activeUserId,users}) {


    const [title, setTitle] = useState("");
    const [chatters, setChatters] = useState([...users]);
    const [chatter, setChatter] = useState("");

    const navigate = useNavigate()

    async function handelOnSubmit(e){

        e.preventDefault();
        console.log(chatters)
        await fetch("/api/chats", {
            method: "POST",
            body: JSON.stringify({userIdList:chatters,title:title}),
            headers: {
                "Content-Type": "application/json"
            }
        });
        navigate("/user")
    }

    function handelAddUser(e) {
        e.preventDefault();
        setChatters(prevState => [...prevState, chatter])
    }

    function setSelectedUser(e) {
        e.preventDefault();
        setChatter(e.target.value);



    }

    return (
        <center>
        <div className={"new-chat-card"}>
            <h1>Create New Chat</h1>

            <form onSubmit={handelAddUser}>
                <div className={"input"}>
                    Title:<input value={title} type="text"  className="write-message" placeholder="Add a title for group chat"onChange={(e) => setTitle(e.target.value)}/>
                </div>
                <br/>
                User:
                <select className={"dropdown"} onChange={(e)=>setSelectedUser(e)}>

                    {users.map((option, index) => {
                        if (option.id_user != activeUserId){
                            return <option className={"dropdownNames"} value={option.id_user} key={index}>
                                {option.username}
                            </option>
                        }
                    })}
                </select>
                <button className={"button"} onClick={(e)=>handelAddUser(e)}>Add User</button>
            </form>
            <br/>

            <div className={"selected new-Chat-Container"}>
                Added to chat:
                {chatters.map((id)=>{

                    return <div className={"new-Chat-User"}>{id.username}</div>
                })}

            </div>

            <button className={"button"} onClick={(e)=>handelOnSubmit(e)}>Start Chat</button>



        </div>
        </center>
    )
}