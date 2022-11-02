
import React from 'react'
import ReactDOM from 'react-dom/client'
import {MainNavBar} from "./styleComponents/navbarStyle.jsx";

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

function Profile() {
    return (
        <div>
            <button class={"button"} onClick={()=>alert("test")}>Profiles</button>
        </div>
    );
}

export function NavBar() {
    return(
        <div>
            <MainNavBar>
                <Logo/>
                <Chats/>
                <Posts/>
                <Profile/>
            </MainNavBar>
        </div>

    );
}