import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export function NewChat() {
    const activeUserId = "1";
    const users =[{id_user:1,username:"per"},{id_user:3,username:"pål"},{id_user:2,username:"jadasdads"}]
    const you = activeUserId;
    const [title, setTitle] = useState("");
    const [chatters, setChatters] = useState([...you]);
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
        <div className={"new-chat-card"}>
            <h1>Create New Message</h1>

            <form onSubmit={handelAddUser}>
                <div className={"input"}>
                    Title:<input value={title} type="text"  className="write-message" placeholder="Add a title for group chat"onChange={(e) => setTitle(e.target.value)}/>
                </div>
                <br/>
                User:
                <select className="dropdown" onChange={(e)=>setSelectedUser(e)}>
                    <option>Add user to chat</option>
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

            <div>
                {chatters.map((id)=>{

                    return <h1>{users[id-1].username}</h1>
                })}

            </div>

            <button className={"button"} onClick={(e)=>handelOnSubmit(e)}>Start Chat</button>



        </div>
    )
}