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




    const toggling = () => setIsOpen(!isOpen);

    const onOptionClicked = value => () => {
        setSelectedUser(value);
        setIsOpen(false);

    };

    return (
        <div>
            <div>Load User</div>
            <DropDownContainer>

                <DropDownHeader onClick={toggling}>
                    {selectedUser || "Users"}
                </DropDownHeader>
                {isOpen && (
                    <DropDownListContainer>
                        <DropDownList>
                            {users.map(option => (
                                <ListItem onClick={onOptionClicked(option.username)} id={option.id_user} key={option.id_user}>
                                    {option.username}
                                </ListItem>
                            ))}
                        </DropDownList>
                    </DropDownListContainer>

                )}
            </DropDownContainer>

        </div>

    );
}