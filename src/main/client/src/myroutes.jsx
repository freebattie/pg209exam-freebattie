import {useEffect, useState} from "react";
import { Route, Routes} from "react-router-dom";
import {FrontPage} from "./frontpage.jsx";
import {Header} from "./header";
import {UserPage} from "./userpage";




export function MyRutes() {



    return (
        <div >
                <Routes>
                    <Route path={"/"} element={<FrontPage />}/>
                    <Route path={"/user"} element={<UserPage />}/>
                </Routes>
        </div>
    )

}
