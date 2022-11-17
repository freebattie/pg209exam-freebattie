import {useNavigate} from "react-router-dom";

export function ChatList({chats, getActiveChat}) {
    console.log(chats);
    const navigate = useNavigate();
    function handelNavigate(s) {
        navigate("/newchat")
    }

    return (
        <div className ="flex-chats">
            <ul>
                <button className={"button"} onClick={()=>handelNavigate('/')}>New Message</button>
                {chats.map((option, index) => {
                    return <li onClick={(props)=>getActiveChat(option)} className={"buttonChat"}  tabindex={index} key={index} id={index}>
                        {option.title}
                    </li>
                })}

            </ul>

        </div>
    );
}