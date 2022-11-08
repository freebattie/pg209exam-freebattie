import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {
    DropDownContainer,
    DropDownHeader,
    DropDownList,
    DropDownListContainer, ListItem
} from "./styleComponents/ListAllUsersStyle.jsx";






export function ListAllUsers() {



    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isOpen, setIsOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState("Users");

   /* useEffect(async () => {
        const res = await fetch("/api/user-login");

        setUsers(await res.json());

        setLoading(false);



    }, []);

    if (loading) {

        return (
            <div>Loading...</div>

        )
    }*/


    const options = ['One', 'Two', 'Three', 'Four', 'Five'];
    const onOptionChangeHandler = (event) => {
        console.log("User Selected Value - ", event.target.value)
    }

    const toggling = () => setIsOpen(!isOpen);

    const onOptionClicked = value => () => {
        setSelectedUser(value);
        setIsOpen(false);

    };

    return (
        <div>
            <center>
                <h1>Velg en bruker fra dropdown</h1>
                <img src="static/Logo.png" alt="Girl in a jacket" width="500" height="600"/>


                <select onChange={onOptionChangeHandler}>

                    <option>Please choose one option</option>
                    {options.map((option, index) => {
                        return <option key={index} >
                            {option}
                        </option>
                    })}
                </select>
            </center>

            {/*<DropDownContainer>*/}

            {/*    <DropDownHeader onClick={toggling}>*/}
            {/*        {selectedUser || "Users"}*/}
            {/*    </DropDownHeader>*/}
            {/*    {isOpen && (*/}
            {/*        <DropDownListContainer>*/}
            {/*            <DropDownList>*/}
                            {users.map(option => (
                                <ListItem onClick={onOptionClicked(option.username)} id={option.id_user} key={option.id_user}>
                                    {option.username}
                                </ListItem>
                            ))}
            {/*            </DropDownList>*/}
            {/*        </DropDownListContainer>*/}

            {/*    )}*/}
            {/*</DropDownContainer>*/}

        </div>

    );
}