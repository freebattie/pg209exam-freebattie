import {Link} from "react-router-dom";

export function Header() {
    return (
        <nav>
            <ul>
                <li><Link to='/'>Back to selectUser</Link></li>
                <li><Link to='/user'>Chats</Link></li>


            </ul>
        </nav>

    );
}