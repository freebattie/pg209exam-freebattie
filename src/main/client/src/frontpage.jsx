import imgUrl from "./static/Logo.png";
import { useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";



export function FrontPage({users,setUserTo}) {
    const navigate = useNavigate();

    function handelNavigate(path) {
        navigate(path);
    }

    return (

        <div className={"padding-Top"}>

            <center>

                <img  src={imgUrl} alt="Social Buzz Logo!" width="800" height="300"/>

                <div className={"padding-Top"}>

                </div>

                <select className={"dropdown"} onChange={(e)=>setUserTo(e)}>

                    <option >Select User</option>
                    {users.map((option, index) => {
                        return <option className={"dropdownNames"} value={option.id_user} key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
                <br/>
                <button className={"button"} onClick={()=>handelNavigate("/user")}>Login</button>
                <button className={"button"} onClick={()=>handelNavigate("/newUser")}>New User</button>
            </center>

        </div>

    );
}