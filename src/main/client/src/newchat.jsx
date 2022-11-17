import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export function NewChat({activeUserId,users}) {
    const [dropdownUsers, setDropdownUsers] = useState([]);
    const [selectedUsersId, setSelectedUsersId] = useState([]);
    const [selectedUsersName, setSelectedUsersName] = useState([]);
    const [selectedUserId, setSelectedUserId] = useState("");
    const [selectedUserName, setSelectedUserName] = useState("");
    const [title, setTitle] = useState("");
    useEffect(() => {


        let filteredusers = users.filter((user) =>  { return  activeUserId != user.id_user})
        let filterYou = users.filter((user) =>  { return  activeUserId == user.id_user})
        setSelectedUsersId(prevState => [...prevState,filterYou[0].id_user])

        setDropdownUsers(filteredusers);
    }, []);
    const navigate = useNavigate()
    let selectUsers = [...users];
   
    async function handelOnSubmit(e){

        e.preventDefault();
       
        await fetch("/api/chats", {
            method: "POST",
            body: JSON.stringify({userIdList:selectedUsersId,title:title}),
            headers: {
                "Content-Type": "application/json"
            }
        });
        navigate("/user")
    }

    function handelAddUser(e) {
        e.preventDefault();
        if (selectedUserName.length > 0){
            setSelectedUsersId(prevState => [...prevState, selectedUserId])
            setSelectedUsersName(prevState=>[...prevState,selectedUserName])
            const newUsers = dropdownUsers.filter((user) =>  { return  selectedUserId != user.id_user})
            setDropdownUsers(newUsers);

        }

        setSelectedUserName("")

    }

    function setSelectedUser(e) {
        e.preventDefault();
        setSelectedUserId(e.target.value);
        const user = dropdownUsers.filter((event)=>{
            return event.id_user == e.target.value;
        })

        setSelectedUserName(user[0].username)




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
                    <option >Select User</option>
                    {dropdownUsers.map((option, index) => {

                            return <option className={"dropdownNames"}  value={option.id_user} key={index}>
                                {option.username}
                            </option>

                    })}
                </select>
                <button className={"button"} onClick={(e)=>handelAddUser(e)}>Add User</button>
            </form>
            <br/>

            <div className={"selected new-Chat-Container"}>
                <div className={"headerSelectedUsers"}>Added to chat:</div>

                {selectedUsersName.map((name)=>{

                    return <div className={"new-Chat-User"}>{name}</div>
                })}

            </div>

            <button className={"button"} onClick={(e)=>handelOnSubmit(e)}>Start Chat</button>



        </div>
        </center>
    )
}