import imgUrl from "./static/Logo.png";
import {Link} from "react-router-dom";

export function FrontPage({users,getActiveUser}) {




    return (
        <div>

            <center>

                <img src={imgUrl} alt="Social Buzz Logo!" width="600" height="300"/>

                <div>

                </div>

                <select onChange={(e)=>getActiveUser(e.target)}>

                    <option>Please choose one option</option>
                    {users.map((option, index) => {
                        return <option value={option.id_user} key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
                <br/>
                <button><Link to={"/user"}>Show all items</Link></button>
            </center>
        </div>

    );
}