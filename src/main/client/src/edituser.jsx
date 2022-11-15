import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export function EditUser({activeUserId}) {
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [tlf, setTlf] = useState("");
    const [emails, setEmails] = useState([]);

    const [stock, setStock] = useState(0);
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
        test()
    }, []);
    if (loading){
        return  <h1>LOADING ..</h1>
    }
    async function handleOnSubmit(event) {



        event.preventDefault();


        const tmp={
            id_user: activeUserId,
            name,
            username,
            tlf,
            emails:emails
        }

        const user = JSON.stringify(tmp)
        console.log("users sent")
        console.log(user)
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
    async function handelRemoveEmail(e){
        e.preventDefault();
        const rmvEmail = emails[emails.length-1];

        const newEmails = emails.filter((email,index) =>  { return  index < emails.length-1})
        setEmails(newEmails);
        console.log(emails)
        await fetch("/api/users/emails/"+rmvEmail.id,{
            method: 'DELETE',
            headers: {
                'Content-type': 'application/json'
            }
        })
    }
    async function handelAddMail(e) {
        e.preventDefault();
        const index = emails.length-1;
        const old = [...emails];
        old[index].id

        const params = {
            id: activeUserId
        };

        const test = new URLSearchParams( {id: activeUserId})
         await fetch("/api/users/emails",{
            method: 'POST',
            body: JSON.stringify({id_user:activeUserId}),
            headers: {
                'Content-type': 'application/json'
            }
        })

        const res = await fetch("/api/users/emails?"+new URLSearchParams( {id: activeUserId}));
        const id = await res.json()

        //console.log(id);

        const email ={
            email:"test",
            id:id
        }


        setEmails(prevState => [...prevState, email]);

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