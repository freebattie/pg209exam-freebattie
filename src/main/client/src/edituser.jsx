import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export function EditUser({activeUserId}) {

    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [tlf, setTlf] = useState("");
    const [emails, setEmails] = useState([]);

    useEffect( () => {
        const test = async ()=>{
            const res = await fetch("/api/users/"+activeUserId);

            const editUser = await res.json()

            setName(editUser.name);
            setUsername(editUser.username)
            setTlf(editUser.tlf)
            setEmails(editUser.emails)


            setLoading(false);
        }
        if (activeUserId>0){
            test()
        }
        else{
            //GET NEW USER
            setLoading(false)
        }

    }, []);
    if (loading){
        return  <h1>LOADING ..</h1>
    }
    async function handleOnSubmit(event) {
        console.log("username"+username)

        event.preventDefault();
        if (username.length < 2){
            navigate("/newuser")
        }
        if (activeUserId > 0){
            const tmp={
                id_user: activeUserId,
                name,
                username,
                tlf,
                emails:emails
            }

            const user = JSON.stringify(tmp)

            const test= await fetch("/api/users", {
                method: "PUT",

                body: user,
                headers: {
                    "Content-Type": "application/json"
                }
            });
            if (test.ok)
                navigate("/user");
        }
        else{
            const tmp={
                id_user: -1,
                name,
                username,
                tlf,
                emails:emails
            }
            const user = JSON.stringify(tmp)
            const test= await fetch("/api/users", {
                method: "POST",

                body: user,
                headers: {
                    "Content-Type": "application/json"
                }
            });
            if (test.ok)
                navigate("/");


        }

    }
    async function handelRemoveEmail(e){
        e.preventDefault();
        const rmvEmail = emails[emails.length-1];

        const newEmails = emails.filter((email,index) =>  { return  index < emails.length-1})
        setEmails(newEmails);
       if (activeUserId > 0){
           await fetch("/api/users/emails/"+rmvEmail.id,{
               method: 'DELETE',
               headers: {
                   'Content-type': 'application/json'
               }
           })
       }
       else{

       }

    }
    async function handelAddMail(e) {
        e.preventDefault();


        if(activeUserId > 0){



            const res = await fetch("/api/users/emails?"+new URLSearchParams( {id: activeUserId}));
            const m = await res.json();
            console.log(m)
            const email ={
                email:"test",
                id:m.id
            }
            setEmails(prevState => [...prevState, email]);

        }else{
            const email ={
                email:"test",
                id:-1
            }
            setEmails(prevState => [...prevState, email]);
        }




    }


    function handelSetEmail(e,index) {
        e.preventDefault()
        let newMail = [...emails];

        newMail[index].email = e.target.value;
        setEmails(newMail)

    }

    function handelNavigate(e) {
        e.preventDefault()
        if (activeUserId > 0)
            navigate("/user")
        else
            navigate("/")
    }

    return (
        <div>
           <center><div className={"editUserTilte"}>EDIT USER!</div></center>
            <center>
            <form onSubmit={handleOnSubmit}>
                <div className={"border"}>
                    name:<input className="write-message" placeholder="Enter a Name" value={name} type="text" onChange={(e) => setName(e.target.value)}/>
                </div>
                <div className={"border"}>
                    username:<input className="write-message" placeholder="Enter a Username" value={username} type="text" onChange={(e) => setUsername(e.target.value)}/>
                </div>
                <div className={"border"}>
                    tlf:<input className="write-message" placeholder="Enter a phone nr" value={tlf} type="text" onChange={(e) => setTlf(e.target.value)}/>
                </div>
                <br/>

                <div>
                    {emails.map((email,index)=>{

                        return <div className={"border"}>
                            email {index+1} :<input  type="email" className="write-message" placeholder="Enter a Email" value={email.email}
                                                    onChange={(e) => handelSetEmail(e,index)}/>
                        </div>
                    })}
                    <button className={"button"} onClick={(e)=>handelAddMail(e)}>add email</button><button className={"button"} onClick={(e)=>handelRemoveEmail(e)}>remove email</button>
                </div>

                <button onClick={(e)=>handelNavigate(e)} className={"button"}>Back</button>
                <button type="submit" className={"button"}>Update</button>
            </form>

            </center>
        </div>
    )
}