
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
    const [loading, setLoading] = useState(true);
    const [userName, setUserName] = useState("bjarte");
    const name = activeUser.options[activeUser.value].text;
    return (
        <div>
            <button className={"button"}><Link to={"/user/edituser"}>{name}</Link></button>
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