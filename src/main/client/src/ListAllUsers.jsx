import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {
    DropDownContainer,
    DropDownHeader,
    DropDownList,
    DropDownListContainer, ListItem
} from "./styleComponents/ListAllUsersStyle.jsx";






export function ListAllUsers() {



    const [users, setUsers] = useState(["bjartes","snorre"]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(async () => {
        const res = await fetch("/api/user-login");
        setUsers(await res.json());
        //setUsers({});
        setLoading(false);
    }, []);

    if (loading) {
        return (
            <div>Loading...</div>
        )
    }


    const [isOpen, setIsOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);

    const toggling = () => setIsOpen(!isOpen);

    const onOptionClicked = value => () => {
        setSelectedUser(value);
        setIsOpen(false);
        console.log(selectedUser);
    };
    var test = users.values();
    test.

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
                            {Array.from(users).map(([key, value]) => (
                                <ListItem onClick={onOptionClicked(value)} key={Math.random()}>
                                    {value}
                                </ListItem>
                            ))}
                        </DropDownList>
                    </DropDownListContainer>

                )}
            </DropDownContainer>

        </div>

    );
}