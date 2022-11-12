
import React, {useEffect, useState} from 'react'



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
    const [user, setUser] = useState([]);
    const [loading, setLoading] = useState(true);
    useEffect(async () => {
        const res = await fetch("/api/chats/"+activeUser);

        setUser(await res.json());

        setLoading(false);



    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }
    return (
        <div>
            <button class={"button"} >{activeUser}</button>
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