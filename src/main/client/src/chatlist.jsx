export function ChatList({chats, getActiveChat}) {
    console.log(chats);
    return (
        <div className ="flex-chats">
            <ul>
                {chats.map((option, index) => {
                    return <li onClick={(props)=>getActiveChat(option)} className="button"  key={index}>
                        {option.title}
                    </li>
                })}

            </ul>

        </div>
    );
}