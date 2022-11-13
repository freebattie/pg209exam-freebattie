import imgUrl from "./static/Logo.png";
import { useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";



export function FrontPage({users,setUserTo}) {
    const navigate = useNavigate();

    function handelNavigate() {
        navigate("/user");
    }

    return (

        <div>

            <center>

                <img src={imgUrl} alt="Social Buzz Logo!" width="600" height="300"/>

                <div>

                </div>

                <select onChange={(e)=>setUserTo(e)}>

                    <option>Please choose one option</option>
                    {users.map((option, index) => {
                        return <option value={option.id_user} key={index}>
                            {option.username}
                        </option>
                    })}
                </select>
                <br/>
                <button className={"button"} onClick={()=>handelNavigate()}></button>
            </center>

        </div>

    );
}