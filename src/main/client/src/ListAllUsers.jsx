import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {
    DropDownContainer,
    DropDownHeader,
    DropDownList,
    DropDownListContainer, ListItem
} from "./styleComponents/ListAllUsersStyle.jsx";
import imgUrl from './static/Logo.png'


export function ListAllUsers() {


    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    const [selectedUser, setSelectedUser] = useState("");

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

    const onOptionChangeHandler = (event) => {
        setSelectedUser(event.target.value)
        console.log("User Selected Value - ", selectedUser)
    }


    return (
        <div>
            <center>
                <h1>Velg en bruker fra dropdown</h1>
                <img src={imgUrl} alt="Social Buzz Logo!" width="200" height="100"/>

                <div></div>
                <select onChange={onOptionChangeHandler}>

                    <option>Please choose one option</option>
                    {users.map((option, index) => {
                        return <option key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
            </center>
        </div>

    );
}