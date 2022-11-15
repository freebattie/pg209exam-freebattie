import {useEffect, useState} from "react";

export function NewChat({users,activeUserId}) {
    const [title, setTitle] = useState("");
    const [chatters, setChatters] = useState([]);
    const [chatter, setChatter] = useState("");


    async function handelOnSubmit(){
        console.log(chatters)
        await fetch("/api/chats", {
            method: "POST",
            body: JSON.stringify({usersId:chatters,title:title}),
            headers: {
                "Content-Type": "application/json"
            }
        });
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
        <div>
            <h1>New message</h1>

            <form onSubmit={handelAddUser}>
                <div>
                    Title:<input value={title} type="text" onChange={(e) => setTitle(e.target.value)}/>
                </div>
                <select onChange={(e)=>setSelectedUser(e)}>

                    <option>Add user to chat</option>
                    {users.map((option, index) => {
                        return <option value={option.id_user} key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
                <br/>
                <button className={"button"} onClick={(e)=>handelAddUser(e)}>Add User</button>
            </form>
            <br/>

            <div>
                {chatters.map((id)=>{

                    return <h1>{users[id-1].username}</h1>
                })}

            </div>

            <button className={"button"} onClick={(e)=>handelOnSubmit(e)}>Add User</button>



        </div>
    )
}