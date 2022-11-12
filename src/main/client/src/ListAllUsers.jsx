import { useEffect, useState} from "react";

import imgUrl from './static/Logo.png'





export function ListAllUsers() {




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

    function onOptionChangeHandler(event) {


        console.log("User Selected Value - ", event.target.value)

    }




    return (
        <div>
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