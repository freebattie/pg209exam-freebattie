import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import imgUrl from "./static/Logo.png";



function FrontPage() {


    const [users, setUsers] = useState({});
    const [loading, setLoading] = useState(true);



    useEffect(async () => {
        const res = await fetch("/api/user-login");

        setUsers(await res.json());

        setLoading(false);



    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }

    return (
        <div>
            <button><Link to={"/user"}>Show all items</Link></button>
            <center>
                <h1>Velg en bruker fra dropdown</h1>
                <img src={imgUrl} alt="Social Buzz Logo!" width="200" height="100"/>

                <div>

                </div>

                <select >

                    <option>Please choose one option</option>
                    {users.map((option, index) => {
                        return <option value={option.id_user} key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
            </center>
        </div>

    );

}
function UserPage( ){

    const [chats, setChats] = useState([]);
    const [loading, setLoading] = useState(true);
    useEffect(async () => {
        const res = await fetch("/api/chats/"+1);

        setChats(await res.json());

        setLoading(false);



    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }
    console.log()
    return(

        <div>

            <div className="flex-container">
                <ChatList />

            </div>
        </div>
    );
}
