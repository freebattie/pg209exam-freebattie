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



        event.preventDefault();


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
                id_user: activeUserId,
                name,
                username,
                tlf,
                emails:emails
            }

            const user = JSON.stringify(tmp)


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

    }
    async function handelAddMail(e) {
        e.preventDefault();
        const index = emails.length-1;

        if(activeUserId > 0){
           ;

            const email ={
                email:"test",
                id:id
            }
            setEmails(prevState => [...prevState, email]);
            const res = await fetch("/api/users/emails?"+new URLSearchParams( {id: activeUserId}));


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

    return (
        <div>
            <h1>EDIT USER!</h1>

            <form onSubmit={handleOnSubmit}>
                <div>
                    name:<input value={name} type="text" onChange={(e) => setName(e.target.value)}/>
                </div>
                <div>
                    username:<input value={username} type="text" onChange={(e) => setUsername(e.target.value)}/>
                </div>
                <div>
                    tlf:<input value={tlf} type="text" onChange={(e) => setTlf(e.target.value)}/>
                </div>
                <br/>

                <div>
                    {emails.map((email,index)=>{

                        return <div>
                            email {index+1} :<input  value={email.email} type="text"
                                                    onChange={(e) => handelSetEmail(e,index)}/>
                        </div>
                    })}
                    <button onClick={(e)=>handelAddMail(e)}>add mail</button><button onClick={(e)=>handelRemoveEmail(e)}>remove mail</button>
                </div>


                <button>Update</button>
            </form>

        </div>
    )
}