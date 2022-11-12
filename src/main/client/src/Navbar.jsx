
import React, {useEffect, useState} from 'react'
import {Link} from "react-router-dom";



function Logo() {
    return (
        <div>
            <button class={"button"}>Logo</button>
        </div>
    );
}

function Chats() {
    return (
        <div>
            <button class={"button"}>Chats</button>
        </div>
    );
}

function Posts() {
    return (
        <div>
            <button class={"button"}>Posts</button>
        </div>
    );
}

function Profile({activeUser}) {
   // const [user, setUser] = useState([]);
    const [userName, setUserName] = useState(activeUser.options[activeUser.value].text);
    console.log(activeUser.options[1].text)
    return (
        <div>
            <button className={"button"}><Link to={"/user/edituser"}>{userName}</Link></button>
        </div>
    );
}

export function NavBar({activeUser}) {
    return(
        <div class={"navbar"}>

                <Logo/>
                <Chats/>
                <Posts/>
                <Profile activeUser={activeUser}/>

        </div>

    );
}